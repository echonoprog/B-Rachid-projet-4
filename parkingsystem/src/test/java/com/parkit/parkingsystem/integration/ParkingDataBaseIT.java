package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.model.Ticket;

import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.constants.ParkingType;





import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito; 
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    /** @BeforeEach
    private void setUpPerTest() throws Exception {
     
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }
    **/

    @AfterAll
    private static void tearDown(){

    }

   @Test
public void testParkingACar() throws Exception {
    // Given
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    dataBasePrepareService.clearDataBaseEntries();
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

    // When
    parkingService.processIncomingVehicle();

    // Then
    Ticket ticket = ticketDAO.getTicket("ABCDEF");
    assertEquals("ABCDEF", ticket.getVehicleRegNumber());
    assertNotNull(ticket.getInTime());
    assertNull(ticket.getOutTime());
    assertEquals(0, ticket.getPrice());
}


	
        @Test
        public void testParkingLotExit() {
            // Given
            Ticket newTicket = new Ticket();
            newTicket.setVehicleRegNumber("ABCDEF");
            newTicket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
            newTicket.setInTime(new Date());
            ticketDAO.saveTicket(newTicket);

            ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

            // When
            parkingService.processExitingVehicle();

            // Then
            Ticket ticket = ticketDAO.getTicket("ABCDEF");

            // assertNotNull(ticket.getOutTime());
            assertNotNull(ticket.getPrice());
    
        }



    @Test
    public void testParkingLotExitRecurringUser()throws Exception {
        
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
        
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();
        
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );
        ticket.setInTime(inTime);
        ticketDAO.saveTicket(ticket);
        parkingService.processExitingVehicle();
        
        

        
        assertNotNull(ticket.getOutTime(), "La date de sortie du ticket ne doit pas être nulle");

    }




}
    