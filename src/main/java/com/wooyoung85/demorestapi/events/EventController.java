package com.wooyoung85.demorestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        eventValidator.validate(eventDto, errors);

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }


        Event event = modelMapper.map(eventDto, Event.class);
        Event createdEvent = eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(createdEvent.getId()).toUri();
        return ResponseEntity.created(createUri).body(createdEvent);
    }
}
