package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import org.joda.time.LocalDate;





public class ActivityPersistenceTest {
	private static final String PROVIDER_CODE = "XtremX";
	private static final String PROVIDER_NAME = "Bush Walking";
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 50;
	private static final int CAPACITY = 30;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private ActivityProvider provider;
	private Activity activity;
	private ActivityOffer offer;
	private Booking booking;


	
	
	@Before
	@Atomic(mode = TxMode.WRITE)
	public void setUp(){
	}
	@Test
	public void success() {
		atomicProcess();
		atomicActivityAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		provider = new ActivityProvider(PROVIDER_CODE,PROVIDER_NAME);
		activity = new Activity(this.provider, PROVIDER_NAME, MIN_AGE, MAX_AGE, CAPACITY);
		offer = new ActivityOffer(this.activity, this.begin, this.end);
		booking = new Booking(this.provider,this.offer);
		booking.cancel();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicActivityAssert() {

		Set<ActivityProvider> providers = FenixFramework.getDomainRoot().getActivityProviderSet();
		provider = providers.iterator().next();
		
		Set<Activity> activities = provider.getActivitySet();
		activity = activities.iterator().next();
		
		/*ActivityProvider */
		assertEquals(1,providers.size());
		assertEquals(PROVIDER_CODE,provider.getCode());
		assertEquals(PROVIDER_NAME,provider.getName());
		
		/*Activity*/
		assertEquals(PROVIDER_NAME, activity.getActivityProvider().getName());
		assertEquals(PROVIDER_CODE, activity.getActivityProvider().getCode());
		assertEquals(PROVIDER_NAME, activity.getName());
		Assert.assertTrue(activity.getCode().startsWith(PROVIDER_CODE));
		Assert.assertTrue(activity.getCode().length() > ActivityProvider.CODE_SIZE);
		assertEquals(MIN_AGE, activity.getMinAge());
		assertEquals(MAX_AGE, activity.getMaxAge());
		assertEquals(CAPACITY, activity.getCapacity());
		
		/*Activity Offer*/
		assertEquals(begin, offer.getBegin());
		assertEquals(end, offer.getEnd());
		assertEquals(CAPACITY, offer.getActivity().getCapacity());
		assertEquals(PROVIDER_NAME, offer.getActivity().getName());
		assertEquals(PROVIDER_CODE, offer.getActivity().getActivityProvider().getCode());
		assertEquals(activity.getCode(), offer.getActivity().getCode());
		
		/*Booking*/
		
		assertEquals(PROVIDER_NAME, booking.getActivityOffer().getActivity().getName());
		assertEquals(PROVIDER_CODE, offer.getActivity().getActivityProvider().getCode());
		Assert.assertTrue(booking.getReference().startsWith(PROVIDER_CODE));
		Assert.assertTrue(booking.getReference().length() > ActivityProvider.CODE_SIZE);
		Assert.assertTrue(booking.getCancel().startsWith("CANCEL"+PROVIDER_CODE));
		Assert.assertTrue(booking.getCancel().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals(new LocalDate(),booking.getCancellationDate());
		
		
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			provider.delete();
		}
	}
}

