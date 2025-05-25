package com.example.msa_backend.service.weather;

import com.example.msa_backend.domain.FutureWeatherLog;
import com.example.msa_backend.domain.WeatherLog;
import com.example.msa_backend.repository.FutureWeatherRepository;
import com.example.msa_backend.repository.WeatherRepository;
import com.example.msa_backend.web.dto.weather.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final FutureWeatherRepository futureWeatherRepository;

    @Override
    public WeatherResponseDTO.PastWeatherDTO getWeather(LocalDate date) {
        WeatherLog weather = weatherRepository.findTopByDate(date);

        return WeatherResponseDTO.PastWeatherDTO.toDTO(weather);
    }

    @Override
    public List<WeatherResponseDTO.FutureWeatherDTO> getFutureWeather() {
        LocalDate today = LocalDate.now();
        LocalDate fiveDaysLater = today.plusDays(5);

        // 해당 기간 동안의 특정 시간대 예보만 가져오기
        List<WeatherLog> logs = futureWeatherRepository.findAllByDateBetween(today.plusDays(1), fiveDaysLater);


        return logs.stream()
                .map(WeatherResponseDTO.FutureWeatherDTO::toDTO)
                .collect(Collectors.toList());
    }

    public String[] getWeatherFromMA(int x, int y) {
        HttpURLConnection con = null;
        String[] v = new String[5]; // 날짜, 시간, 날씨, 기온, 습도

        try {
            LocalDateTime t = LocalDateTime.now().minusMinutes(30);
//            LocalDateTime t = LocalDateTime.of(2025, 5, 11, 18, 0);

            URL url = new URL(
                    "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"
                            + "?ServiceKey=GDKUMNE%2BvPikShtxJFu9tp1VA2Liur1b8slhGukqlLiEVIAHJqkVX9HCVyo0ZS2gFkFWS52J6dijBE89oR%2FsBQ%3D%3D"  // 서비스 키
                            + "&numOfRows=60"
                            + "&base_date=" + t.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            + "&base_time=" + t.format(DateTimeFormatter.ofPattern("HHmm"))
                            + "&nx=" + x
                            + "&ny=" + y
            );

            con = (HttpURLConnection) url.openConnection();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());

            boolean ok = false;
            NodeList headers = doc.getElementsByTagName("header");
            if (headers.getLength() > 0) {
                Element e = (Element) headers.item(0);
                if ("00".equals(e.getElementsByTagName("resultCode").item(0).getTextContent()))
                    ok = true;
            }

            if (ok) {
                String fd = null, ft = null, pty = null, sky = null;

                NodeList items = doc.getElementsByTagName("item");
                for (int i = 0; i < items.getLength(); i++) {
                    Element e = (Element) items.item(i);

                    if (ft == null) {
                        fd = e.getElementsByTagName("fcstDate").item(0).getTextContent();
                        ft = e.getElementsByTagName("fcstTime").item(0).getTextContent();
                    } else if (!fd.equals(e.getElementsByTagName("fcstDate").item(0).getTextContent()) ||
                            !ft.equals(e.getElementsByTagName("fcstTime").item(0).getTextContent())) {
                        continue;
                    }

                    String cat = e.getElementsByTagName("category").item(0).getTextContent();
                    String val = e.getElementsByTagName("fcstValue").item(0).getTextContent();

                    if ("PTY".equals(cat)) pty = val;
                    else if ("SKY".equals(cat)) sky = val;
                    else if ("T1H".equals(cat)) v[3] = val;
                    else if ("REH".equals(cat)) v[4] = val;
                }

                v[0] = fd;
                v[1] = ft;

                if ("0".equals(pty)) {
                    if ("1".equals(sky)) v[2] = "맑음";
                    else if ("3".equals(sky)) v[2] = "구름많음";
                    else if ("4".equals(sky)) v[2] = "흐림";
                } else if ("1".equals(pty)) v[2] = "비";
                else if ("2".equals(pty)) v[2] = "비/눈";
                else if ("3".equals(pty)) v[2] = "눈";
                else if ("5".equals(pty)) v[2] = "빗방울";
                else if ("6".equals(pty)) v[2] = "빗방울눈날림";
                else if ("7".equals(pty)) v[2] = "눈날림";

            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

            weatherRepository.save(WeatherLog.builder()
                    .date(LocalDate.parse(v[0], dateFormatter))      // 예: "20250525"
                    .time(LocalTime.parse(v[1], timeFormatter))      // 예: "0900"
                    .status(v[2])
                    .temperature(Double.valueOf(v[3]))
                    .humidity(Double.valueOf(v[4]))
                    .build());

            log.info("🌤 날씨 정보 저장 완료: {} {} {} {}℃ {}%", v[0], v[1], v[2], v[3], v[4]);

        } catch (Exception e) {
            log.error("🌧 날씨 정보 처리 중 오류 발생", e);
            return null;
        } finally {
            if (con != null) con.disconnect();
        }

        return v;
    }

    public void getShortTermForecastFromMA(int x, int y) {
        HttpURLConnection con = null;

        try {
            LocalDateTime t = LocalDateTime.now();
            String baseDate = t.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String baseTime = "0200";

            URL url = new URL(
                    "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
                            + "?ServiceKey=GDKUMNE%2BvPikShtxJFu9tp1VA2Liur1b8slhGukqlLiEVIAHJqkVX9HCVyo0ZS2gFkFWS52J6dijBE89oR%2FsBQ%3D%3D"
                            + "&numOfRows=1000"
                            + "&dataType=XML"
                            + "&base_date=" + baseDate
                            + "&base_time=" + baseTime
                            + "&nx=" + x
                            + "&ny=" + y
            );

            con = (HttpURLConnection) url.openConnection();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());

            NodeList items = doc.getElementsByTagName("item");

            String currentDate = "", currentTime = "", sky = "", pty = "", tmp = "", reh = "";

            for (int i = 0; i < items.getLength(); i++) {
                Element e = (Element) items.item(i);

                String fcstDate = e.getElementsByTagName("fcstDate").item(0).getTextContent();
                String fcstTime = e.getElementsByTagName("fcstTime").item(0).getTextContent();
                String category = e.getElementsByTagName("category").item(0).getTextContent();
                String value = e.getElementsByTagName("fcstValue").item(0).getTextContent();

                if (!fcstDate.equals(currentDate) || !fcstTime.equals(currentTime)) {
                    if (!currentTime.equals("")) {
                        saveOrUpdateFutureWeather(currentDate, sky, pty, tmp, reh);
                    }

                    currentDate = fcstDate;
                    currentTime = fcstTime;
                    sky = pty = tmp = reh = "";
                }

                switch (category) {
                    case "TMP" -> tmp = value;
                    case "REH" -> reh = value;
                    case "SKY" -> sky = value;
                    case "PTY" -> pty = value;
                }
            }

            // 마지막 항목도 처리
            if (!currentTime.equals("")) {
                saveOrUpdateFutureWeather(currentDate, sky, pty, tmp, reh);
            }

            log.info("📅 단기 예보 저장 및 업데이트 완료");

        } catch (Exception e) {
            log.error("⚠️ 단기 예보 처리 중 오류 발생", e);
        } finally {
            if (con != null) con.disconnect();
        }
    }

    private void saveOrUpdateFutureWeather(String date, String sky, String pty, String tmp, String reh) {
        String status = convertSkyAndPty(sky, pty);

        FutureWeatherLog existing = futureWeatherRepository.findTopByDate(LocalDate.parse(date));

        if (existing != null) {
            boolean changed = false;

            if (!equalsSafe(existing.getStatus(), status)) {
                existing.setStatus(status);
                changed = true;
            }

            if (!equalsSafe(String.valueOf(existing.getTemperature()), tmp)) {
                existing.setTemperature(Double.valueOf(tmp));
                changed = true;
            }

            if (!equalsSafe(String.valueOf(existing.getHumidity()), reh)) {
                existing.setHumidity(Double.valueOf(reh));
                changed = true;
            }

            if (changed) {
                futureWeatherRepository.save(existing);
                log.info("🔁 기존 예보 수정됨: {} → {}, {}℃, {}%", date, status, tmp, reh);
            }
        } else {
            futureWeatherRepository.save(FutureWeatherLog.builder()
                    .date(LocalDate.parse(date))
                    .status(status)
                    .temperature(Double.valueOf(tmp))
                    .humidity(Double.valueOf(reh))
                    .build());
            log.info("🆕 새 예보 저장: {} → {}, {}℃, {}%", date, status, tmp, reh);
        }
    }

    private boolean equalsSafe(String a, String b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

    private String convertSkyAndPty(String sky, String pty) {
        if (pty != null && !"0".equals(pty)) {
            return switch (pty) {
                case "1" -> "비";
                case "2" -> "비/눈";
                case "3" -> "눈";
                case "5" -> "빗방울";
                case "6" -> "빗방울눈날림";
                case "7" -> "눈날림";
                default -> "강수";
            };
        }

        return switch (sky) {
            case "1" -> "맑음";
            case "3" -> "구름많음";
            case "4" -> "흐림";
            default -> "미정";
        };
    }
}
