package com.example.ppkwulab3.endpoint;

import com.example.ppkwulab3.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class CalendarEndpoint {

    private CalendarService calendarService;

    @Autowired
    public CalendarEndpoint(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("calendar/{year}/{month}")
    public ResponseEntity<Resource> getCalendarEvents(@PathVariable String year, @PathVariable String month) throws IOException {
        return calendarService.getCalendarEvents(year, month);
    }
}
