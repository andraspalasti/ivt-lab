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

  @Test
  public void firstPrimaryFired() {
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockSecondayTorpedoStore, times(0)).fire(1);
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void alternatingFiring() {
    ship.fireTorpedo(FiringMode.SINGLE);

    // Arrange
    when(mockSecondayTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondayTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockSecondayTorpedoStore, times(1)).fire(1);
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void noPrimaryTorpedo() {
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondayTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondayTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockSecondayTorpedoStore, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void failPrimaryTorpedo() {
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondayTorpedoStore, times(0)).fire(1);
    assertEquals(false, result);
  }

  @Test
  public void fireBothStores() {
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondayTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondayTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondayTorpedoStore, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void bothStoresEmpty() {
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondayTorpedoStore.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorpedoStore, times(0)).fire(1);
    verify(mockSecondayTorpedoStore, times(0)).fire(1);
    assertEquals(false, result);
  }

  @Test
  public void secondaryStoreEmpty() {
    // Arrange
    when(mockSecondayTorpedoStore.isEmpty()).thenReturn(true);
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(2)).fire(1);
    verify(mockSecondayTorpedoStore, times(0)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void bothStoresEmptySingle() {
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondayTorpedoStore.isEmpty()).thenReturn(true);

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(0)).fire(1);
    verify(mockSecondayTorpedoStore, times(0)).fire(1);
    assertEquals(false, result1);
    assertEquals(false, result2);
  }

  @Test
  public void primaryEmptyAfterOneShot() {
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondayTorpedoStore.isEmpty()).thenReturn(true);

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);

    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondayTorpedoStore, times(0)).fire(1);
    assertEquals(true, result1);
    assertEquals(false, result2);
  } 

  @Test
  public void foreTorpedoDefaultCase() {
    boolean result = ship.fireTorpedo(FiringMode.NONE);
    assertEquals(false, result);
  }
}
