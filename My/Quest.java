import java.util.ArrayList;

import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 * Quest related methods
 * 
 * @author Cakemix
 */
public class Quests {

	/**
	 * @return Quest Quest[Name, Progress, WidgetChild]
	 * @see Quest
	 */
	public static Quest getQuest(String questName) {
		ArrayList<Quest> quests = getQuests();
		for (Quest quest : quests)
			if (quest.getName().equals(questName))
				return quest;
		return null;
	}

	/**
	 * @return ArrayList<Quest> ArrayList of Quests
	 * @see Quest
	 */
	public static ArrayList<Quest> getQuests() {
		ArrayList<Quest> list = new ArrayList<Quest>();
		WidgetChild[] widgetChilds = Widgets.get(WIDGET_QUEST, WIDGET_QUESTS).getChildren();

		for (WidgetChild widgetChild : widgetChilds) {

			String widgetChildText = widgetChild.getText();

			if (widgetChild == null && !widgetChild.validate()
					|| (widgetChildText.trim().isEmpty() || widgetChild.getTextColor() == 16750848))
				continue;

			Progress progress = getProgress(widgetChild.getTextColor());

			list.add(new Quest(widgetChildText, progress, widgetChild));
		}

		return list;
	}

	/**
	 * @param color
	 *            Text color
	 * @return Progress
	 */
	private static Progress getProgress(int color) {
		switch (color) {
		case 16711680:
			return Progress.NOT_STARTED;
		case 16776960:
			return Progress.IN_PROGRESS;
		case 65280:
			return Progress.COMPLETED;
		default:
			return Progress.UNKOWN;
		}
	}

	/**
	 * @param questName
	 *            Quest name
	 * @return <tt>true</tt> if quest is not started; otherwise <tt>false</tt>.
	 */
	public static boolean isNotStarted(String questName) {
		ArrayList<Quest> quests = getQuests();
		for (Quest quest : quests)
			if (quest.getName().equalsIgnoreCase(questName) && quest.getProgress().equals(Progress.NOT_STARTED))
				return true;
		return false;
	}

	/**
	 * @param questName
	 *            Quest name
	 * @return <tt>true</tt> if quest is in progress; otherwise <tt>false</tt>.
	 */
	public static boolean isInProgress(String questName) {
		ArrayList<Quest> quests = getQuests();
		for (Quest quest : quests)
			if (quest.getName().equalsIgnoreCase(questName) && quest.getProgress().equals(Progress.IN_PROGRESS))
				return true;
		return false;
	}

	/**
	 * @param questName
	 *            Quest name
	 * @return <tt>true</tt> if quest is completed; otherwise <tt>false</tt>.
	 */
	public static boolean isCompleted(String questName) {
		ArrayList<Quest> quests = getQuests();
		for (Quest quest : quests)
			if (quest.getName().equalsIgnoreCase(questName) && quest.getProgress().equals(Progress.COMPLETED))
				return true;
		return false;
	}

	/**
	 * @param questName Quest name
	 * @return <tt>true</tt> if quest found and scrolled to; otherwise
	 *         <tt>false</tt>.
	 */
	public static boolean scrollToQuest(String questName) {
		return scrollToQuest(getQuest(questName).getWidgetChild());
	}

	/**
	 * @param widgetChild
	 *            Widget child
	 * @return <tt>true</tt> if quest found and scrolled to; otherwise
	 *         <tt>false</tt>.
	 */
	public static boolean scrollToQuest(WidgetChild widgetChild) {
		final WidgetChild scrollBar = Widgets.get(WIDGET_QUEST, WIDGET_SCROLLBAR);
		return scrollBar != null && widgetChild != null && scrollBar.validate() && Widgets.scroll(widgetChild, scrollBar);
	}

	public static class Quest {
		private final String name;
		private final Progress progress;
		private final WidgetChild widgetChild;

		/**
		 * @param name
		 *            Quest name
		 * @param progress
		 *            Quest progress
		 * @param widgetChild
		 *            Quest widget
		 */
		public Quest(String name, Progress progress, WidgetChild widgetChild) {
			this.name = name;
			this.progress = progress;
			this.widgetChild = widgetChild;
		}

		/**
		 * @return String Quest name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return Progress Quest progress
		 */
		public Progress getProgress() {
			return progress;
		}

		/**
		 * @return WidgetChild Quest widget child
		 */
		public WidgetChild getWidgetChild() {
			return widgetChild;
		}

		/**
		 * @return String [name, progress, widgetChild]
		 */
		@Override
		public String toString() {
			return "Quest [name=" + name + ", progress=" + progress + ", widgetChild=" + widgetChild + "]";
		}

	}

	public enum Progress {
		NOT_STARTED,
		IN_PROGRESS,
		COMPLETED,
		UNKOWN
	}

	public static final int WIDGET_QUEST = 190;
	public static final int WIDGET_SHOWING = 12;
	public static final int WIDGET_SCROLLBAR = 14;
	public static final int WIDGET_QUESTS = 15;
	public static final int WIDGET_POINTS = 25;
	public static final int WIDGET_BUTTON_FILTER = 3;
	public static final int WIDGET_BUTTON_HIDE_DONE = 7;
}