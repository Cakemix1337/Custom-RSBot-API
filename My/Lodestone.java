import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 * Lodestone system
 * 
 * Note: Needs manual home telport click.
 * 
 * @author Cakemix
 * 
 */
public enum Lonestone {
	BANDIT_CAMP(7),
	LUNAR_ISLE(39),
	AL_KHARID(40),
	ARDOUGNE(41),
	BURTHORPE(42),
	CATHERBY(43),
	DRAYNOR_VILLAGE(44),
	EDGEVILLE(45),
	FALADOR(46),
	LUMBRIDGE(47),
	PORT_SARIM(48),
	SEERS_VILLAGE(49),
	TAVERLEY(50),
	VARROCK(51),
	YANILLE(52);

	private final int currentLodeStone;

	Lonestone(int currentLodeStone) {
		this.currentLodeStone = currentLodeStone;
	}

	/**
	 * Closes the lodestone system.
	 * 
	 * @return <tt>true</tt> if lodestone system was closed; otherwise
	 *         <tt>false</tt>
	 */
	public static boolean close() {
		return Widgets.get(WIDGET, WIDGET_BUTTON_CLOSE).interact("Close");
	}

	/**
	 * Teleports the user to the destination
	 * 
	 * @return <tt>true</tt> if user can teleport, the lodestone system is open
	 *         or if the mouse was clicked; otherwise <tt>false</tt>.
	 */
	public boolean teleport() {
		if (canTeleport()) {
			WidgetChild widget = Widgets.get(WIDGET, getCurrentLodeStone());
			int x = widget.getAbsoluteX();
			int y = widget.getAbsoluteY();
			int width = widget.getWidth();
			int height = widget.getHeight();

			if (Mouse.move(x + (width / 2) + Random.nextInt(2, 4), y + (height / 2) + Random.nextInt(2, 4)))
				return Mouse.click(true);
		}
		return false;
	}

	/**
	 * Checks if user can teleport to destination.
	 * 
	 * @return <tt>true</tt> if user can teleport or the lodestone system is
	 *         open; otherwise <tt>false</tt>.
	 */
	public boolean canTeleport() {
		if (Widgets.get(WIDGET, getCurrentLodeStone()).hover()) {
			WidgetChild widget = Widgets.get(WIDGET, WIDGET_CHECK);
			if (!widget.validate() && !widget.visible())
				return false;

			System.out.println(Widgets.get(WIDGET, WIDGET_CHECK).getText().contains("is not yet active"));

			if (widget.getText().toLowerCase().startsWith(toString().toLowerCase().substring(0, 4).replace("_", " ")))
				return !Widgets.get(WIDGET, WIDGET_CHECK_TEXT).getText().contains("is not yet active");
		}
		return false;
	}

	/**
	 * @return the current Lodestone
	 */
	public int getCurrentLodeStone() {
		return currentLodeStone;
	}

	public static int WIDGET = 1092;
	public static int WIDGET_BUTTON_CLOSE = 60;
	public static int WIDGET_CHECK = 65, WIDGET_CHECK_TEXT = 66;
}