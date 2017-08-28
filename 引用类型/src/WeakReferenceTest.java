
import java.lang.ref.WeakReference;

public class WeakReferenceTest {

	public static void main(String[] args) {
		WeakReference ref = new WeakReference(new MyDate());
        System.gc(); 
	}
}
