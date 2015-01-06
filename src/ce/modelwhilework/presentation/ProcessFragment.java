package ce.modelwhilework.presentation;

import ce.modelwhilework.data.Card;
import ce.modelwhilework.data.Message;
import ce.modelwhilework.data.Process;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.Task;
import android.os.Bundle;
import android.content.ClipData;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ProcessFragment extends Fragment {

	final String MAINSTACK = "MAINSTACK", SIDESTACK = "SIDESTACK",
			MSGCARD = "MSGCARD", TASKCARD = "TASKCARD";
	
	private View fragment;
	RelativeLayout rl_MainStack, rl_MainStackTaskCard, rl_MainStackMsgCard, rl_SideStack,
				   rl_SideStackTaskCard, rl_SideStackMsgCard, rl_TaskCard, rl_MsgCard;
	
	Process process;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragment = inflater.inflate(R.layout.fragment_process, container, false);
		
		rl_TaskCard = (RelativeLayout) fragment.findViewById(R.id.LayoutTaskCard);
		rl_MsgCard = (RelativeLayout) fragment.findViewById(R.id.LayoutMsgCard);
		
		rl_MainStack = (RelativeLayout) fragment.findViewById(R.id.LayoutMainStack);
		rl_MainStackTaskCard = (RelativeLayout) fragment.findViewById(R.id.LayoutMainStackTaskCard);
		rl_MainStackMsgCard = (RelativeLayout) fragment.findViewById(R.id.LayoutMainStackMsgCard);		
		
		rl_SideStack = (RelativeLayout) fragment.findViewById(R.id.LayoutSideStack);
		rl_SideStackTaskCard = (RelativeLayout) fragment.findViewById(R.id.LayoutSideStackTaskCard);
		rl_SideStackMsgCard = (RelativeLayout) fragment.findViewById(R.id.LayoutSideStackMsgCard);
		
		ImageView iv_bin = (ImageView) fragment.findViewById(R.id.imageViewBin);

		// set tag definitions
		rl_TaskCard.setTag(TASKCARD);
		rl_MsgCard.setTag(MSGCARD);
		rl_MainStack.setTag(MAINSTACK);
		rl_SideStack.setTag(SIDESTACK);

		// Current Process
		Bundle args = this.getArguments();
		args.getString("ProcessName");
		process = ProcessManager.getInstance().getProcess(args.getString("ProcessName"));
		
		// set touch listeners
		rl_TaskCard.setOnTouchListener(new ChoiceTouchListener());
		rl_MsgCard.setOnTouchListener(new ChoiceTouchListener());

		// set drag listeners
		rl_MainStack.setOnDragListener(new ChoiceDragListener());
		iv_bin.setOnDragListener(new BinDragListener());
		
		ImageButton imgButtonContext = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfo);
		imgButtonContext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), ContextInfoActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			    startActivity(intent);
			}
		});
		
		updateView();
		
		return fragment;
	}
	
	public void addContextInformation(View v) {

	}
	
	public void showContextInformation(View v) {

	}

	private final class ChoiceTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				/*
				 * Drag details: we only need default behavior - clip data could
				 * be set to pass data as part of drag - shadow can be tailored
				 */
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				// start dragging the item touched
				view.startDrag(data, shadowBuilder, view, 0);
				return true;
			} else {
				return false;
			}
		}
	}

	private final class DoNothingTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			return true;
		}
	}

	private final class DoNothinDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			return false;
		}
	}

	private final class ChoiceDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// no action necessary
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				// no action necessary
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				// no action necessary
				break;
			case DragEvent.ACTION_DROP:

				// get card (dragged element)
				View view = (View) event.getLocalState();
				RelativeLayout dropElement = (RelativeLayout) view;

				// get the stack (drag target element)
				RelativeLayout targetElement = (RelativeLayout) v;

				// is this a card of a stack or a ney one?
				String dropTag = (String) dropElement.getTag();
				String targetTag = (String) targetElement.getTag();
				if (dropTag.equals(MSGCARD) || dropTag.equals(TASKCARD)) {

					// new card
					Card dataCard;
					if (dropTag.equals(MSGCARD)) {
						dataCard = new Message("a message");
					} else {
						dataCard = new Task("a simple task");
					}

					if (!process.addCard(dataCard)) {
						// todo: set alert
					}

				} else if (dropTag.equals(MAINSTACK)
						&& targetTag.equals(SIDESTACK)) {

					// card from main stack --> move it to side stack
					if (!process.putCardAside()) {
						// todo: set alter
					}
				} else if (dropTag.equals(SIDESTACK)
						&& targetTag.equals(MAINSTACK)) {

					// card from main stack --> move it to side stack
					if (!process.putBackFromAside()) {
						// todo: set alter
					}
				}

				updateView();

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				// no action necessary
				break;
			default:
				break;
			}
			return true;
		}
	}
	
	private final class BinDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// no action necessary
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				// no action necessary
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				// no action necessary
				break;
			case DragEvent.ACTION_DROP:

				// get card (dragged element)
				View view = (View) event.getLocalState();
				RelativeLayout dropElement = (RelativeLayout) view;

				//todo: set question: do you really want to delete the card?
				String dropTag = (String) dropElement.getTag();
				if (dropTag.equals(MAINSTACK)) {

					if (!process.removeCardFromMainStack()) {
						// todo: set alter
					}
				} else if (dropTag.equals(SIDESTACK)) {

					if (!process.removeCardFromSideStack()) {
						// todo: set alter
					}
				}
				
				updateView();

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				// no action necessary
				break;
			default:
				break;
			}
			return true;
		}
	}

	private void updateView() {

		TextView tv_processTitle = (TextView) fragment.findViewById(R.id.textViewProcessTitle);
		tv_processTitle.setText(process.getTitle());

		// display main and side stack

		TextView tv_Main = (TextView) fragment.findViewById(R.id.textViewMainStack);
		TextView tv_Side = (TextView) fragment.findViewById(R.id.textViewSideStack);
		
		Card card = process.getTopCardMainStack();
		if (card != null) {

			if(card.isMessage()) {
				rl_MainStackTaskCard.setVisibility(View.INVISIBLE);
				rl_MainStackMsgCard.setVisibility(View.VISIBLE);
				tv_Main.setVisibility(View.INVISIBLE);
			} else if(card.isTask()) {
				rl_MainStackTaskCard.setVisibility(View.VISIBLE);
				rl_MainStackMsgCard.setVisibility(View.INVISIBLE);
				tv_Main.setVisibility(View.INVISIBLE);
			} else {
				//todo: error
			}
			
			rl_MainStack.setOnTouchListener(new ChoiceTouchListener());
			rl_SideStack.setOnDragListener(new ChoiceDragListener());			
			
		} else {

			rl_MainStackTaskCard.setVisibility(View.INVISIBLE);
			rl_MainStackMsgCard.setVisibility(View.INVISIBLE);
			tv_Main.setVisibility(View.VISIBLE);			
			rl_MainStack.setOnTouchListener(new DoNothingTouchListener());
			rl_SideStack.setOnDragListener(new DoNothinDragListener());
		}
		
		card = process.getTopCardSideStack();
		if (card != null) {

			if(card.isMessage()) {
				rl_SideStackTaskCard.setVisibility(View.INVISIBLE);
				rl_SideStackMsgCard.setVisibility(View.VISIBLE);
				tv_Side.setVisibility(View.INVISIBLE);
			} else if(card.isTask()) {
				rl_SideStackTaskCard.setVisibility(View.VISIBLE);
				rl_SideStackMsgCard.setVisibility(View.INVISIBLE);
				tv_Side.setVisibility(View.INVISIBLE);
			} else {
				//todo: error
			}
			
			rl_SideStack.setOnTouchListener(new ChoiceTouchListener());	
			
		} else {

			rl_SideStackTaskCard.setVisibility(View.INVISIBLE);
			rl_SideStackMsgCard.setVisibility(View.INVISIBLE);
			tv_Side.setVisibility(View.VISIBLE);			
			rl_SideStack.setOnTouchListener(new DoNothingTouchListener());
		}
	}
}
