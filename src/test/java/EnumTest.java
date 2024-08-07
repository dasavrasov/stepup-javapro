import org.junit.Test;
import ru.stepup.entity.ProductType;


public class EnumTest {
    @Test
    public void testEnum() {
        String code = "0";
        ProductType productType = ProductType.valueOf(code);
        System.out.println(productType.getDescription());
    }
}
