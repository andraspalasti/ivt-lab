package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimaryTorpedoStore;
  private TorpedoStore mockSecondayTorpedoStore;

  @BeforeEach
  public void init() {
    mockPrimaryTorpedoStore = mock(TorpedoStore.class);
    mockSecondayTorpedoStore = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPrimaryTorpedoStore, mockSecondayTorpedoStore);
  }

  @Test
  public void fireTorpedo_Single_Success() {
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success() {
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondayTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondayTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
    verify(mockSecondayTorpedoStore, times(1)).fire(1);
    verify(mockSecondayTorpedoStore, times(1)).isEmpty();
    assertEquals(true, result);
  }

}
