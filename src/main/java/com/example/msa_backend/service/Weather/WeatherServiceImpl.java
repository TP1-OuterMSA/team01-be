package com.example.msa_backend.service.Weather;

import com.example.msa_backend.domain.WeatherLog;
import com.example.msa_backend.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class WeatherServiceImpl implements  WeatherService {

    private final WeatherRepository weatherRepository;


    public String[] getWeather(int x, int y) {
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

            weatherRepository.save(WeatherLog.builder()
                    .date(v[0])
                    .time(v[1])
                    .status(v[2])
                    .temperature(v[3])
                    .humidity(v[4])
                    .build());

            log.info("🌤 날씨 정보 저장 완료: {} {} {} {}℃ {}%", v[0], v[1], v[2], v[3], v[4]);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (con != null) con.disconnect();
        }

        return v;
    }
}
