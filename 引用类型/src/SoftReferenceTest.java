
import java.lang.ref.SoftReference;

public class SoftReferenceTest {

	public static void main(String[] args) {
		SoftReference<MyDate> ref = new SoftReference<MyDate>(new MyDate());
		ReferenceTest.drainMemory();
	}
}
