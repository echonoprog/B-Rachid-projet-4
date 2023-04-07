
package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
            //when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            //ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            //Ticket ticket = new Ticket();
            //ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            //ticket.setParkingSpot(parkingSpot);
            //ticket.setVehicleRegNumber("ABCDEF");
            // when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            // when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            // when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingVehicleTest(){
        try {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        when(ticketDAO.getNbTicket(anyString())).thenReturn(1);
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
    }
    }
    
     @Test
    public void testProcessIncomingVehicle() {

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processIncomingVehicle();
        
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		
    }
    
    @Test
    public void processExitingVehicleTestUnableUpdate() {
        
        try {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        
        // when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        // when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);


        parkingService.processExitingVehicle();

        verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
        //verify(ticketDAO, Mockito.times(0)).saveTicket(any(Ticket.class));
		verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
		
    } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
    }
    }

    
    @Test
    public void testGetNextParkingNumberIfAvailable() {
	
	
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        ParkingSpot nextParkingNumberIfAvailable = parkingService.getNextParkingNumberIfAvailable();
        
        assertEquals(nextParkingNumberIfAvailable.getId(),1); // code à refaire
		
    }
    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);

        // ParkingSpot nextParkingNumberIfAvailable = parkingService.getNextParkingNumberIfAvailable();
        assertNull(parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument(){

        when(inputReaderUtil.readSelection()).thenReturn(3);

        ParkingSpot nextParkingNumberIfAvailable = parkingService.getNextParkingNumberIfAvailable();
        
        assertNull(nextParkingNumberIfAvailable);

    }
}