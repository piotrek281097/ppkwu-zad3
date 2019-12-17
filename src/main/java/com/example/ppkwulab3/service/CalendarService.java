package com.example.ppkwulab3.service;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class CalendarService {

    public ResponseEntity<Resource> getCalendarEvents(String year, String month) throws IOException {
        String urlCalendar ="http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php?rok=" + year + "&miesiac=" + month + "&lang=1";
        Document document;
        ICalendar iCalendar = new ICalendar();
        Calendar calendar = Calendar.getInstance();

        document = Jsoup.connect(urlCalendar).get();
        Elements daysEvents = document.select("a.active");
        Elements events = document.select("div.InnerBox");

        Map<String, String> eventsMap = new HashMap();

        for(int i = 0; i < daysEvents.size(); i++) {
            String day = daysEvents.get(i).text();
            String event = events.get(i).text();
            eventsMap.put(day, event);
        }

        for (String key : eventsMap.keySet()) {
            calendar.set(Calendar.YEAR, Integer.parseInt(year));
            calendar.set(Calendar.MONTH, (Integer.parseInt(month) - 1));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(key));

            VEvent event = new VEvent();
            event.setSummary(eventsMap.get(key));
            event.setDateStart(new DateStart(calendar.getTime()));
            event.setDateEnd(new DateEnd(calendar.getTime()));

            iCalendar.addEvent(event);
        }

        File file = new File("myCalendar.ics");
        Biweekly.write(iCalendar).go(file);
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/calendar"))
                .body(resource);
    }
}
