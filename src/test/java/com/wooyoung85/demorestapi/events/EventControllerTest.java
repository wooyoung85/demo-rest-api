package com.wooyoung85.demorestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooyoung85.demorestapi.common.RestDocsConfiguration;
import com.wooyoung85.demorestapi.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
            .name("Spring")
            .description("Test")
            .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 29, 11, 20))
            .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .beginEventDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .endEventDateTime(LocalDateTime.of(2019, 12, 1, 11, 20))
            .basePrice(100)
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("Test")
            .build();

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
            .andExpect(jsonPath("free").value(false))
            .andExpect(jsonPath("offline").value(true))
            .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.query-events").exists())
            .andExpect(jsonPath("_links.update-event").exists())
            .andDo(document("create-event",
                links(
                    linkWithRel("self").description("link to self"),
                    linkWithRel("query-events").description("link to query events"),
                    linkWithRel("update-event").description("link to update an existing"),
                    linkWithRel("profile").description("link to update an existing")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                ),
                requestFields(
                    fieldWithPath("name").description("Name of new event"),
                    fieldWithPath("description").description("description of new event"),
                    fieldWithPath("beginEnrollmentDateTime").description("date time of beginEnrollmentDateTime"),
                    fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollmentDateTime"),
                    fieldWithPath("beginEventDateTime").description("date time of beginEventDateTime"),
                    fieldWithPath("endEventDateTime").description("date time of endEventDateTime"),
                    fieldWithPath("location").description("location of new event"),
                    fieldWithPath("basePrice").description("base price of new event"),
                    fieldWithPath("maxPrice").description("max price of new event"),
                    fieldWithPath("limitOfEnrollment").description("limit of new event")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.LOCATION).description("Location header"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                ),
                responseFields(
                    fieldWithPath("id").description("identifier of new event"),
                    fieldWithPath("name").description("Name of new event"),
                    fieldWithPath("description").description("description of new event"),
                    fieldWithPath("beginEnrollmentDateTime").description("date time of beginEnrollmentDateTime"),
                    fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollmentDateTime"),
                    fieldWithPath("beginEventDateTime").description("date time of beginEventDateTime"),
                    fieldWithPath("endEventDateTime").description("date time of endEventDateTime"),
                    fieldWithPath("location").description("location of new event"),
                    fieldWithPath("basePrice").description("base price of new event"),
                    fieldWithPath("maxPrice").description("max price of new event"),
                    fieldWithPath("limitOfEnrollment").description("limit of new event"),
                    fieldWithPath("free").description("it tells if this event is free or not"),
                    fieldWithPath("offline").description("it tells if this event is offline or not"),
                    fieldWithPath("eventStatus").description("event status"),
                    fieldWithPath("_links.self.href").description("link to self"),
                    fieldWithPath("_links.query-events.href").description("link to query events"),
                    fieldWithPath("_links.update-event.href").description("link to update event"),
                    fieldWithPath("_links.profile.href").description("link to profile")
                )
            ))
        ;
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
            .id(100)
            .name("Spring")
            .description("Test")
            .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 29, 11, 20))
            .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .beginEventDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .endEventDateTime(LocalDateTime.of(2019, 12, 1, 11, 20))
            .basePrice(100)
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("Test")
            .build();

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andDo(print())
            .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
            .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
            .name("Spring")
            .description("Test")
            .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 29, 11, 20))
            .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 28, 11, 20))
            .beginEventDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .endEventDateTime(LocalDateTime.of(2019, 11, 29, 11, 20))
            .basePrice(100)
            .maxPrice(10)
            .limitOfEnrollment(100)
            .location("Test")
            .build();

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsString(eventDto)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("content[0].objectName").exists())
            .andExpect(jsonPath("content[0].defaultMessage").exists())
            .andExpect(jsonPath("content[0].code").exists())
            .andExpect(jsonPath("_links.index").exists())
        ;
    }
}