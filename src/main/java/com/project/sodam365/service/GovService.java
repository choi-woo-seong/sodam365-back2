package com.project.sodam365.service;

import com.project.sodam365.entity.Gov;
import com.project.sodam365.repository.GovRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GovService {

    private final GovRepository govRepository;
    private final com.project.sodam365.repository.FavoriteRepository favoriteRepository;

    public void fetchAndSaveGovData() {
        try {
            String apiUrl = "https://apis.data.go.kr/B553701/LoanProductSearchingInfo/LoanProductSearchingInfo/getLoanProductSearchingInfo?"
                    + "serviceKey=%2B8dvMKyqmV8vBeR63PIMdxhJ%2B6S56dMfo2IC%2FQahCML911LU7YoZDcOdm31zJeLlsZrrzCldxM6%2FtVaPCdVU8Q%3D%3D"
                    + "&pageNo=1"
                    + "&numOfRows=100"
                    + "&type=xml";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String xmlData = response.body();

            List<Gov> govList = parseGovData(xmlData);
            govRepository.saveAll(govList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Gov> parseGovData(String xml) throws Exception {
        List<Gov> list = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);

        NodeList itemList = doc.getElementsByTagName("item");

        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);

            Gov gov = new Gov();
            gov.setFinPrdNm(getTagValue("finprdnm", item));
            gov.setIrt(getTagValue("irt", item));
            gov.setLnLmt(getTagValue("lnlmt", item));
            gov.setTrgt(getTagValue("trgt", item));
            gov.setHdlInst(getTagValue("hdlinst", item));
            gov.setMaxTotLnTrm(getTagValue("maxtotlntrm", item));
            gov.setRdptMthd(getTagValue("rdptmthd", item));

            gov.setG_title("정부지원대출");
            gov.setUsge(getTagValue("usge", item));

            list.add(gov);
        }

        return list;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() == 0) return null;
        Node node = nodeList.item(0);
        return node != null ? node.getTextContent() : null;
    }

    public List<Gov> getAllGovData() {
        // 정부지원 데이터는 삭제 기능이 없지만, 찜에서 삭제할 수 있게 대비
        // favoriteRepository.deleteByTargetIdAndTargetType(..., FavoriteType.GOV);
        return govRepository.findAll();
    }

    public Gov getGovById(Long id) {
        return govRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 데이터가 존재하지 않습니다. ID = " + id));
    }
}
