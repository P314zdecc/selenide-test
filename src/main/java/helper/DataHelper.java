package helper;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataHelper {
    private static List<String> getSearchProduct() {
        List<String> product = new ArrayList<>();
        product.add("iPhone");
        product.add("IPhone");
        product.add("IPHONE");
        return product;
    }

    private static List<String> getManufacturer() {
        List<String> manufacturer = new ArrayList<>();
        manufacturer.add("Apple");
        return manufacturer;
    }

    /**
     * @return
     * @autor Сергей Лебедев
     * 1й аргумент - manufacturer
     * 2й аргумент - searchProduct
     * 3й аргумент - category
     * 4й аргумент - section
     * 4й аргумент - filter
     */
    public static Stream<Arguments> parameterTest() {
        String category = "Электроника";
        String section = "Смартфоны и гаджеты";
        String filter = "Бренд";
        return Stream.of(
                Arguments.of(getManufacturer(), getSearchProduct(), category, section, filter)
        );
    }


}
