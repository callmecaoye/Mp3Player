package com.caoye.xml;

import com.caoye.model.Mp3Info;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/12/16.
 */
public class Mp3ListContentHandler extends DefaultHandler {
    private List<Mp3Info> infos = new ArrayList<>();
    private Mp3Info info = null;
    private String tagName = null;

    public List<Mp3Info> getInfos() {
        return infos;
    }
    public void setInfos(List<Mp3Info> infos) {
        this.infos = infos;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String data = new String(ch, start, length);

        if (tagName.equals("id")) {
            info.setId(data);
        } else if (tagName.equals("mp3.name")) {
            info.setMp3Name(data);
        } else if (tagName.equals("mp3.size")) {
            info.setMp3Size(data);
        } else if (tagName.equals("lrc.name")) {
            info.setLrcName(data);
        } else if (tagName.equals("lrc.size")) {
            info.setLrcSize(data);
        }

    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.tagName = localName;
        if (tagName.equals("resource")) {
            info = new Mp3Info();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("resource")) {
            infos.add(info);
        }
        tagName = null;
    }
}
