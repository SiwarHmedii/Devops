import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.services.IEventServices;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IEventServices eventServices;

    @InjectMocks
    private EventRestController eventRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventRestController).build();
    }

    @Test
    void testAddParticipant() throws Exception {
        // Arrange
        Participant participant = new Participant();
        participant.setNom("John");
        participant.setPrenom("Doe");

        when(eventServices.addParticipant(any(Participant.class))).thenReturn(participant);

        // Act & Assert
        mockMvc.perform(post("/event/addPart")
                        .contentType("application/json")
                        .content("{\"nom\":\"John\", \"prenom\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("John"))
                .andExpect(jsonPath("$.prenom").value("Doe"));

        verify(eventServices, times(1)).addParticipant(any(Participant.class));
    }

    @Test
    void testAddEvent() throws Exception {
        // Arrange
        Event event = new Event();
        event.setDescription("Sample Event");
        event.setDateDebut(LocalDate.now());
        event.setDateFin(LocalDate.now().plusDays(1));
        event.setCout(100.0f);

        when(eventServices.addAffectEvenParticipant(any(Event.class))).thenReturn(event);

        // Act & Assert
        mockMvc.perform(post("/event/addEvent")
                        .contentType("application/json")
                        .content("{\"description\":\"Sample Event\", \"dateDebut\":\"2025-02-01\", \"dateFin\":\"2025-02-02\", \"cout\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Sample Event"))
                .andExpect(jsonPath("$.cout").value(100.0));

        verify(eventServices, times(1)).addAffectEvenParticipant(any(Event.class));
    }

    @Test
    void testAddEventPart() throws Exception {
        // Arrange
        Event event = new Event();
        event.setDescription("Sample Event");
        event.setDateDebut(LocalDate.now());
        event.setDateFin(LocalDate.now().plusDays(1));
        event.setCout(100.0f);
        int participantId = 1;

        when(eventServices.addAffectEvenParticipant(any(Event.class), eq(participantId))).thenReturn(event);

        // Act & Assert
        mockMvc.perform(post("/event/addEvent/{id}", participantId)
                        .contentType("application/json")
                        .content("{\"description\":\"Sample Event\", \"dateDebut\":\"2025-02-01\", \"dateFin\":\"2025-02-02\", \"cout\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Sample Event"));

        verify(eventServices, times(1)).addAffectEvenParticipant(any(Event.class), eq(participantId));
    }

    @Test
    void testAddAffectLog() throws Exception {
        // Arrange
        Logistics logistics = new Logistics();
        logistics.setDescription("Logistics details");

        when(eventServices.addAffectLog(any(Logistics.class), eq("Sample Event Description"))).thenReturn(logistics);

        // Act & Assert
        mockMvc.perform(put("/event/addAffectLog/{description}", "Sample Event Description")
                        .contentType("application/json")
                        .content("{\"description\":\"Logistics details\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Logistics details"));

        verify(eventServices, times(1)).addAffectLog(any(Logistics.class), eq("Sample Event Description"));
    }

    @Test
    void testGetLogistiquesDates() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.of(2025, 2, 1);
        LocalDate endDate = LocalDate.of(2025, 2, 28);
        Logistics logistics = new Logistics();
        logistics.setDescription("Logistics for event");

        when(eventServices.getLogisticsDates(eq(startDate), eq(endDate))).thenReturn(List.of(logistics));

        // Act & Assert
        mockMvc.perform(get("/event/getLogs/{d1}/{d2}", startDate, endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Logistics for event"));

        verify(eventServices, times(1)).getLogisticsDates(eq(startDate), eq(endDate));
    }

    @Test
    void testGetParReservLogis() throws Exception {
        // Arrange
        Participant participant = new Participant();
        participant.setNom("John");

        when(eventServices.getParReservLogis()).thenReturn(List.of(participant));

        // Act & Assert
        mockMvc.perform(get("/event/getParticipantsLogis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("John"));

        verify(eventServices, times(1)).getParReservLogis();
    }
}
