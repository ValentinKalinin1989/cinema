package database;


import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class testHallTable {

    @Test
    public void whenFindFreePlace() {
        InteractionWithPostgresDb database = InteractionWithPostgresDb.getInstance();
        boolean result = database.placeIsFree(1, 1);
        assertThat(result, is(true));
    }

    @Test
    public void whenTryToBlockedPlace() {
        InteractionWithPostgresDb database = InteractionWithPostgresDb.getInstance();
        database.blockedPlace(1, 2);
        boolean result = database.placeIsFree(1, 2);
        assertThat(result, is(false));
    }

    @Test
    public void whenTryToBuyPlace() {
        InteractionWithPostgresDb database = InteractionWithPostgresDb.getInstance();
        database.buyPlace(1, 3);
        boolean result = database.placeIsFree(1, 3);
        assertThat(result, is(false));
    }

    @Test
    public void whenTryFindIdSeatInDB() {
        InteractionWithPostgresDb database = InteractionWithPostgresDb.getInstance();
        int id = database.findIdPlaceByRowAndColumn(2, 3);
        assertThat(id, is(11));
    }

    @Test
    public void whenTryBuySeatAndInfoToDB() {
        InteractionWithPostgresDb database = InteractionWithPostgresDb.getInstance();
        database.buyPlaceAndAddBuyerToDB(2, 8, "Тестовый покупатель вотрой", "8 (961) 277-90-99");
    }
}
