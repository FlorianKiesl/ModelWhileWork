package ce.modelwhilework.presentation;

import ce.modelwhilework.data.Card;
import ce.modelwhilework.data.CardAttribute;
import ce.modelwhilework.data.Message;
import ce.modelwhilework.data.Process;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.Task;
import ce.modelwhilework.presentation.contextinfo.ContextInfoActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class ProcessFragment extends Fragment implements DialogInterface.OnClickListener {
	
	private View fragment;
	private View l_MainStack, l_MainStackTaskCard, l_MainStackMsgCard, l_SideStack,
				           l_SideStackTaskCard, l_SideStackMsgCard, l_TaskCard, l_MsgCard;
	private CheckBox cb_Sender, cb_Reciver, cb_SenderMainStack, cb_ReciverMainStack, cb_SenderSideStack, cb_ReciverSideStack;
	private EditText te_MainStackTaskTitle, te_SideStackTaskTitle, te_MainStackMsgTitle, te_SideStackMsgTitle,
					 te_MainStackMsgPerson, te_SideStackMsgPerson, te_TaskTitle, te_MsgTitle, te_MsgSenderReciver,
					 te_Role;
	private TextView tv_processTitle, tv_MainStackCount, tv_SideStackCount;
	private ImageButton ib_Process, ib_TaskCard, ib_MsgCard, ib_TaskCardMain, ib_MsgCardMain, ib_TaskCardSide, ib_MsgCardSide;
	private Process process;
	private boolean dragAndDropActive = false;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        
		fragment = inflater.inflate(R.layout.fragment_process, container, false);
		
		l_TaskCard = (View) fragment.findViewById(R.id.LayoutTaskCard);
		l_MsgCard = (View) fragment.findViewById(R.id.LayoutMsgCard);
		
		l_MainStack = (View) fragment.findViewById(R.id.LayoutMainStack);
		l_MainStackTaskCard = (View) fragment.findViewById(R.id.LayoutMainStackTaskCard);
		l_MainStackMsgCard = (View) fragment.findViewById(R.id.LayoutMainStackMsgCard);		
		
		l_SideStack = (View) fragment.findViewById(R.id.LayoutSideStack);
		l_SideStackTaskCard = (View) fragment.findViewById(R.id.LayoutSideStackTaskCard);
		l_SideStackMsgCard = (View) fragment.findViewById(R.id.LayoutSideStackMsgCard);
				
		ImageView iv_bin = (ImageView) fragment.findViewById(R.id.imageViewBin);

		// set tag definitions
		l_TaskCard.setTag(CardAttribute.TASKCARD.toString());
		l_MsgCard.setTag(CardAttribute.MSGCARD.toString());
		l_MainStack.setTag(CardAttribute.MAINSTACK.toString());
		l_SideStack.setTag(CardAttribute.SIDESTACK.toString());

		// Current Process
		Bundle args = this.getArguments();
		args.getString("ProcessName");
		process = ProcessManager.getInstance().getProcess(args.getString("ProcessName"));
		
		// set touch listeners
		l_TaskCard.setOnTouchListener(new ChoiceTouchListener());
		l_MsgCard.setOnTouchListener(new ChoiceTouchListener());

		// set drag listeners
		l_MainStack.setOnDragListener(new ChoiceDragListener());
		iv_bin.setOnDragListener(new BinDragListener());
		
		ib_Process = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoProcess);
		ib_Process.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), ContextInfoActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			    startActivity(intent);
			}
		});
		
		ImageButton imgButtonFavorite = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_favorite);
		imgButtonFavorite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), FavoriteActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			    startActivity(intent);
			}
		});
		
		ib_MsgCardMain = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardMsg_MainStack);		
		ib_MsgCardMain.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(process.getTopCardMainStack().getTitle().length() > 0) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), ContextInfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CARD_ID", process.getTopCardMainStack().getTitle());
				    startActivity(intent);
				}
				else
					showAlert("Please enter a valid title!!!");
				
			}
		});
		
		ib_TaskCardMain = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardTask_MainStack);
		ib_TaskCardMain.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(process.getTopCardMainStack().getTitle().length() > 0) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), ContextInfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CARD_ID", process.getTopCardMainStack().getTitle());
				    startActivity(intent);
				}
				else
					showAlert("Please enter a valid title!!!");
				
			}
		});

		ib_MsgCardSide = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardMsg_SideStack);
		ib_MsgCardSide.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(process.getTopCardSideStack().getTitle().length() > 0) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), ContextInfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CARD_ID", process.getTopCardSideStack().getTitle());
				    startActivity(intent);
				}
				else
					showAlert("Please enter a valid title!!!");
				
			}
		});
		
		ib_TaskCardSide = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardTask_SideStack);
		ib_TaskCardSide.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(process.getTopCardSideStack().getTitle().length() > 0) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), ContextInfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CARD_ID", process.getTopCardSideStack().getTitle());
				    startActivity(intent);
				}
				else
					showAlert("Please enter a valid title!!!");
				
			}
		});
		
		ib_MsgCard = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardMsg);
		ib_MsgCard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(process.getMessageCard().getTitle().length() > 0) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), ContextInfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CARD_ID", process.getMessageCard().getTitle());
				    startActivity(intent);
				}
				else
					showAlert("Please enter a valid title!!!");
				
			}
		});
		
		ib_TaskCard = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardTask);
		ib_TaskCard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(process.getTaskCard().getTitle().length() > 0) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), ContextInfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CARD_ID", process.getTaskCard().getTitle());
				    startActivity(intent);
				}
				else
					showAlert("Please enter a valid title!!!");
				
			}
		});

		tv_processTitle = (TextView) fragment.findViewById(R.id.textViewProcessTitle);	
		te_Role = (EditText) fragment.findViewById(R.id.fragment_process_textEdit_role);	
		
		tv_MainStackCount = (TextView) fragment.findViewById(R.id.fragment_process_mainStack_textView_Count);
		tv_SideStackCount = (TextView) fragment.findViewById(R.id.fragment_process_sideStack_textView_Count);
		
		te_TaskTitle = (EditText) fragment.findViewById (R.id.editTextWorkCardTitle);
		te_MsgTitle = (EditText) fragment.findViewById (R.id.editTextMsgCardTitle);
		te_MsgSenderReciver = (EditText) fragment.findViewById (R.id.editTextMsgCardReciverSender);
		
		te_MainStackTaskTitle = (EditText) fragment.findViewById (R.id.editTextWorkCardTitleMainStack);
		te_SideStackTaskTitle = (EditText) fragment.findViewById (R.id.editTextWorkCardTitleSideStack);
		te_MainStackMsgTitle = (EditText) fragment.findViewById (R.id.editTextMsgCardTitleMainStack);
		te_SideStackMsgTitle = (EditText) fragment.findViewById (R.id.editTextMsgCardTitleSideStack);
		te_MainStackMsgPerson = (EditText) fragment.findViewById (R.id.editTextMsgCardReciverSenderMainStack);
		te_SideStackMsgPerson = (EditText) fragment.findViewById (R.id.editTextMsgCardReciverSenderSideStack);
		
		cb_Sender = (CheckBox) fragment.findViewById (R.id.checkBoxMsgCardSend);
		cb_Reciver = (CheckBox) fragment.findViewById (R.id.checkBoxMsgCardRecive);		
		cb_SenderMainStack = (CheckBox) fragment.findViewById (R.id.checkBoxMsgCardSendMainstack);
		cb_ReciverMainStack = (CheckBox) fragment.findViewById (R.id.checkBoxMsgCardReciveMainStack);
		cb_SenderSideStack = (CheckBox) fragment.findViewById (R.id.checkBoxMsgCardSendSideStack);
		cb_ReciverSideStack = (CheckBox) fragment.findViewById (R.id.checkBoxMsgCardReciveSideStack);
		
		te_Role.setKeyListener(null);
		te_Role.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), TextInputActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("CardAttrib", CardAttribute.USERROLE.toString());
				intent.putExtra("ProcessName", process.getTitle());
				intent.putExtra("DefaultText", te_Role.getText().toString());	
			    startActivity(intent);
			}
			
		});
		
		//######################### task card input #######################################################
		
		te_TaskTitle.setKeyListener(null);
		te_TaskTitle.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), TextInputActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("CardAttrib", CardAttribute.TITLETASK.toString());
				intent.putExtra("ProcessName", process.getTitle());
				intent.putExtra("DefaultText", te_TaskTitle.getText().toString());	
			    startActivity(intent);					
			}
		});
		
				
		//#################################################################################################
				
		//######################### msg card input ########################################################
		
		te_MsgTitle.setKeyListener(null);
		te_MsgTitle.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				
				if(!dragAndDropActive) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), TextInputActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CardAttrib", CardAttribute.TITLEMSG.toString());
					intent.putExtra("ProcessName", process.getTitle());
					intent.putExtra("DefaultText", te_MsgTitle.getText().toString());	
				    startActivity(intent);
				}
			}
		});		
		
		cb_Sender.setOnClickListener(new OnClickListener() {

		      @Override
		      public void onClick(View v) {
		    	  if(cb_Sender.isChecked() != process.getMessageCard().isSender()) {
		    		  process.getMessageCard().setSender(cb_Sender.isChecked());
			    	  updateView();
		    	  }		    			 
		      }
		  });
		
		cb_Reciver.setOnClickListener(new OnClickListener() {

		      @Override
		      public void onClick(View v) {
		    	  if(cb_Reciver.isChecked() == process.getMessageCard().isSender()) {
		    		  process.getMessageCard().setSender(!cb_Reciver.isChecked());
			    	  updateView();
		    	  }
		      }
		  });
		
		te_MsgSenderReciver.setKeyListener(null);
		te_MsgSenderReciver.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				
				if(!dragAndDropActive) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), TextInputActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CardAttrib", CardAttribute.PERSONMSG.toString());
					intent.putExtra("ProcessName", process.getTitle());
					intent.putExtra("DefaultText", te_MsgSenderReciver.getText().toString());	
				    startActivity(intent);
				}
			}
		});		
		
		//#################################################################################################		
		
		//######################### task card main stack ##################################################

		te_MainStackTaskTitle.setKeyListener(null);
		te_MainStackTaskTitle.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				
				if(!dragAndDropActive) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), TextInputActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CardAttrib", CardAttribute.TITLEMAINSTACK.toString());
					intent.putExtra("ProcessName", process.getTitle());
					intent.putExtra("DefaultText", te_MainStackTaskTitle.getText().toString());	
				    startActivity(intent);
				}
			}
		});
		
		//#################################################################################################
		
		
		//########################## msg card main stack ##################################################
		
		te_MainStackMsgTitle.setKeyListener(null);
		te_MainStackMsgTitle.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				
				if(!dragAndDropActive) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), TextInputActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CardAttrib", CardAttribute.TITLEMAINSTACK.toString());
					intent.putExtra("ProcessName", process.getTitle());
					intent.putExtra("DefaultText", te_MainStackMsgTitle.getText().toString());	
				    startActivity(intent);
				}
			}
		});
		
		cb_SenderMainStack.setOnClickListener(new OnClickListener() {

		      @Override
		      public void onClick(View v) { 
		    	  
		    	  if(process.getTopCardMainStack() != null) {
		    		  if(cb_SenderMainStack.isChecked() != ((Message)process.getTopCardMainStack()).isSender()) {
			    		  ((Message)process.getTopCardMainStack()).setSender(cb_SenderMainStack.isChecked());
				    	  updateView();
			    	  }
		    	  }
		      }
		  });
		
		cb_ReciverMainStack.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {
				 if(process.getTopCardMainStack() != null) {
					 if(cb_ReciverMainStack.isChecked() == ((Message)process.getTopCardMainStack()).isSender()) {
						 ((Message)process.getTopCardMainStack()).setSender(!cb_ReciverMainStack.isChecked());
				    	  updateView();
					 }
				 }
			 }
		  });
		
		te_MainStackMsgPerson.setKeyListener(null);
		te_MainStackMsgPerson.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				
				if(!dragAndDropActive) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), TextInputActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CardAttrib", CardAttribute.PERSONMAINSTACK.toString());
					intent.putExtra("ProcessName", process.getTitle());
					intent.putExtra("DefaultText", te_MainStackMsgPerson.getText().toString());	
				    startActivity(intent);
				}
			}
		});
		
		//#################################################################################################

		//######################### task card side stack ##################################################
		
		te_SideStackTaskTitle.setKeyListener(null);
		te_SideStackTaskTitle.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				
				if(!dragAndDropActive) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), TextInputActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CardAttrib", CardAttribute.TITLESIDESTACK.toString());
					intent.putExtra("ProcessName", process.getTitle());
					intent.putExtra("DefaultText", te_SideStackTaskTitle.getText().toString());	
				    startActivity(intent);
				}
			}
		});
		
		//#################################################################################################
		
		//########################## msg card side stack ##################################################
		
		te_SideStackMsgTitle.setKeyListener(null);
		te_SideStackMsgTitle.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				
				if(!dragAndDropActive) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), TextInputActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CardAttrib", CardAttribute.TITLESIDESTACK.toString());
					intent.putExtra("ProcessName", process.getTitle());
					intent.putExtra("DefaultText", te_SideStackMsgTitle.getText().toString());	
				    startActivity(intent);
				}
			}
		});
		
		cb_SenderSideStack.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) { 
				 
				 if(process.getTopCardSideStack() != null) {
					 if(cb_SenderSideStack.isChecked() != ((Message)process.getTopCardSideStack()).isSender()) {
						 ((Message)process.getTopCardSideStack()).setSender(cb_SenderSideStack.isChecked());
				    	  updateView();
					 }		
				 }		 
			 }
		  });
		
		cb_ReciverSideStack.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {
				 
				 if(cb_ReciverSideStack.isChecked() != ((Message)process.getTopCardSideStack()).isSender()) {
					 ((Message)process.getTopCardSideStack()).setSender(!cb_ReciverSideStack.isChecked());
			    	  updateView();
				 }				 
			 }
		  });
		
		te_SideStackMsgPerson.setKeyListener(null);
		te_SideStackMsgPerson.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				
				if(!dragAndDropActive) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), TextInputActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CardAttrib", CardAttribute.PERSONSIDESTACK.toString());
					intent.putExtra("ProcessName", process.getTitle());
					intent.putExtra("DefaultText", te_SideStackMsgPerson.getText().toString());	
				    startActivity(intent);
				}
			}
		});
		
		//#################################################################################################
		
		initializeEditTexts();
		
		return fragment;
	}
	
	public void onPause() {
		   super.onPause();
	};  

	public void onResume() {
		   super.onResume();	   
		   updateView();
	};	
	
	private final class ChoiceTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
	
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);

				view.startDrag(data, shadowBuilder, view, 0);
				dragAndDropActive = true;
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
				View dropElement = (View) view;

				// get the stack (drag target element)
				View targetElement = (View) v;

				// is this a card of a stack or a ney one?
				String dropTag = (String) dropElement.getTag();
				String targetTag = (String) targetElement.getTag();
				if (dropTag.equals(CardAttribute.MSGCARD.toString()) || dropTag.equals(CardAttribute.TASKCARD.toString())) {

					// new card
					Card dataCard = null;
					if (dropTag.equals(CardAttribute.MSGCARD.toString())) {			
												
						if(process.getMessageCard().getTitle().length() == 0) {	showAlert("card has no title!"); }
						else if( process.getMessageCard().getSenderReceiver().length() == 0) {
							if(process.getMessageCard().isSender()) { showAlert("card has no sender!");	}
							else { showAlert("card has no reciver!"); }
						}
						else
							dataCard = process.getMessageCard();
					} else {
						
						
						if(process.getTaskCard().getTitle().length() == 0) {
							showAlert("card has no title!");
						}
						else
							dataCard = process.getTaskCard();
					}

					if(dataCard != null) {
						if (process.addCard(dataCard)) {
							
							if(dataCard instanceof Message){
								process.setMessageCard(new Message("", "", true));
								ProcessManager.getInstance().addFavoriteMessage((Message) dataCard);
							}
							
							else if(dataCard instanceof Task){
								process.setTaskCard(new Task(""));
								ProcessManager.getInstance().addFavoriteTask((Task) dataCard);
							}	
						}
						else
							showAlert("add card to stack fail... title must be unique!");
					}

				} else if (dropTag.equals(CardAttribute.MAINSTACK.toString())
						&& targetTag.equals(CardAttribute.SIDESTACK.toString())) {

					// card from main stack --> move it to side stack
					if (!process.putCardAside()) {
						showAlert("move card fail!!!");
					}
				} else if (dropTag.equals(CardAttribute.SIDESTACK.toString())
						&& targetTag.equals(CardAttribute.MAINSTACK.toString())) {

					// card from main stack --> move it to side stack
					if (!process.putBackFromAside()) {
						showAlert("move card fail!!!");
					}
				}
				
				dragAndDropActive = false;
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
				View dropElement = (View) view;

				//set question: do you really want to delete the card?
				String dropTag = (String) dropElement.getTag();
				showDeleteQuestion("Are you positive to delete the card?", dropTag);
				
				dragAndDropActive = false;

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
	
	private void initializeEditTexts() {
		
		FocusAble(te_MainStackTaskTitle, false);
		FocusAble(te_SideStackTaskTitle, false);
		FocusAble(te_MainStackMsgTitle, false);
		FocusAble(te_SideStackMsgTitle, false);
		FocusAble(te_MainStackMsgPerson, false);
		FocusAble(te_SideStackMsgPerson, false);
		FocusAble(te_TaskTitle , false);
		FocusAble(te_MsgTitle, false);
		FocusAble(te_MsgSenderReciver, false);
		FocusAble(te_Role, false);	
	}
	
	private void FocusAble(EditText te, boolean b) {
		
		te.setFocusable(b);             
		te.setFocusableInTouchMode(b);             
		//te.setClickable(b); 
	}
	
	private void updateView() {
				
		tv_processTitle.setText(process.getTitle());
		te_Role.setText(process.getUserRole());
		
		if(process.hasContextInformation())
			ib_Process.setImageResource(R.drawable.context64);
		else
			ib_Process.setImageResource(R.drawable.context64empty);

		// display main and side stack		
		Card card = process.getTopCardMainStack();
		if (card != null) {

			if(card instanceof Message) {
				l_MainStackTaskCard.setVisibility(View.INVISIBLE);
				l_MainStackMsgCard.setVisibility(View.VISIBLE);

				if(card.hasContextInformation())
					ib_MsgCardMain.setImageResource(R.drawable.context32);
				else
					ib_MsgCardMain.setImageResource(R.drawable.context32empty);
				
				te_MainStackMsgTitle.setText(card.getTitle());
				te_MainStackMsgTitle.setSelection(te_MainStackMsgTitle.getText().length());
				te_MainStackMsgPerson.setText(((Message)card).getSenderReceiver());
				te_MainStackMsgPerson.setSelection(te_MainStackMsgPerson.getText().length());
				cb_SenderMainStack.setChecked(((Message)card).isSender());
				cb_ReciverMainStack.setChecked(!((Message)card).isSender());				
			} else if(card instanceof Task) {
				l_MainStackTaskCard.setVisibility(View.VISIBLE);
				
				if(card.hasContextInformation())
					ib_TaskCardMain.setImageResource(R.drawable.context32);
				else
					ib_TaskCardMain.setImageResource(R.drawable.context32empty);
				
				l_MainStackMsgCard.setVisibility(View.INVISIBLE);
				te_MainStackTaskTitle.setText(card.getTitle());		
				te_MainStackTaskTitle.setSelection(te_MainStackTaskTitle.getText().length());
			} else {
				showAlert("invalid card type!!!");
			}
			
			l_MainStack.setOnTouchListener(new ChoiceTouchListener());
			l_SideStack.setOnDragListener(new ChoiceDragListener());			
			
		} else {
			l_MainStackTaskCard.setVisibility(View.INVISIBLE);
			l_MainStackMsgCard.setVisibility(View.INVISIBLE);		
			l_MainStack.setOnTouchListener(new DoNothingTouchListener());
			l_SideStack.setOnDragListener(new DoNothinDragListener());
		}
		
		card = process.getTopCardSideStack();
		if (card != null) {

			if(card instanceof Message) {
				l_SideStackTaskCard.setVisibility(View.INVISIBLE);
				l_SideStackMsgCard.setVisibility(View.VISIBLE);
				
				if(card.hasContextInformation())
					ib_MsgCardSide.setImageResource(R.drawable.context32);
				else
					ib_MsgCardSide.setImageResource(R.drawable.context32empty);
				
				te_SideStackMsgTitle.setText(card.getTitle());
				te_SideStackMsgTitle.setSelection(te_SideStackMsgTitle.getText().length());
				te_SideStackMsgPerson.setText(((Message)card).getSenderReceiver());
				te_SideStackMsgPerson.setSelection(te_SideStackMsgPerson.getText().length());
				cb_SenderSideStack.setChecked(((Message)card).isSender());
				cb_ReciverSideStack.setChecked(!((Message)card).isSender());				
			} else if(card instanceof Task) {
				l_SideStackTaskCard.setVisibility(View.VISIBLE);
				
				if(card.hasContextInformation())
					ib_TaskCardSide.setImageResource(R.drawable.context32);
				else
					ib_TaskCardSide.setImageResource(R.drawable.context32empty);
				
				l_SideStackMsgCard.setVisibility(View.INVISIBLE);
				te_SideStackTaskTitle.setText(card.getTitle());		
				te_SideStackTaskTitle.setSelection(te_SideStackTaskTitle.getText().length());
			} else {
				showAlert("invalid card type!!!");
			}
			
			l_SideStack.setOnTouchListener(new ChoiceTouchListener());	
			
		} else {

			l_SideStackTaskCard.setVisibility(View.INVISIBLE);
			l_SideStackMsgCard.setVisibility(View.INVISIBLE);		
			l_SideStack.setOnTouchListener(new DoNothingTouchListener());
		}
		
		Task task = process.getTaskCard();
		if (task != null) {
			te_TaskTitle.setText(task.getTitle());
			te_TaskTitle.setSelection(te_TaskTitle.getText().length());
			
			if(task.hasContextInformation())
				ib_TaskCard.setImageResource(R.drawable.context32);
			else
				ib_TaskCard.setImageResource(R.drawable.context32empty);
			
		} else {
			showAlert("general error update view!!!");
		}
		
		Message msg = process.getMessageCard();
		if (msg != null) {			
			te_MsgTitle.setText(msg.getTitle());
			te_MsgTitle.setSelection(te_MsgTitle.getText().length());
			te_MsgSenderReciver.setText(msg.getSenderReceiver());
			te_MsgSenderReciver.setSelection(te_MsgSenderReciver.getText().length());			
			cb_Sender.setChecked(msg.isSender());
			cb_Reciver.setChecked(!msg.isSender());	
			
			if(msg.hasContextInformation())
				ib_MsgCard.setImageResource(R.drawable.context32);
			else
				ib_MsgCard.setImageResource(R.drawable.context32empty);
			
		} else {
			showAlert("general error update view!!!");
		}
		
		card = process.getCardAfterTopCardMainStack();
		if(card != null) {
			if(card instanceof Message)
				tv_MainStackCount.setBackgroundResource(R.drawable.messagebackcard);
			else
				tv_MainStackCount.setBackgroundResource(R.drawable.taskbackcard);
			tv_MainStackCount.setText(Integer.toString(process.getMainStackCount()) + " ");
		}
		else
		{
			tv_MainStackCount.setBackgroundResource(R.drawable.withbackground);
			tv_MainStackCount.setText("");
		}
		
		card = process.getCardAfterTopCardSideStack();
		if(card != null) {
			if(card instanceof Message)
				tv_SideStackCount.setBackgroundResource(R.drawable.messagebackcard);
			else
				tv_SideStackCount.setBackgroundResource(R.drawable.taskbackcard);
			tv_SideStackCount.setText(Integer.toString(process.getSideStackCount()) + " ");
		}
		else
		{
			tv_SideStackCount.setBackgroundResource(R.drawable.withbackground);
			tv_SideStackCount.setText("");
		}
	}
	
	private void showAlert(String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("error");
		alertDialog.setNegativeButton("OK", this);
		alertDialog.setMessage(msg);
		alertDialog.show();	
	}
	
	private void showDeleteQuestion(String msg, final String dropTag) {
	
		AlertDialog.Builder alertDialog =	new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("question");
		alertDialog.setMessage(msg);
		alertDialog.setNegativeButton("cancel",
				   new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int whichButton) {
      			    	updateView();
                      }
                }
		);
		alertDialog.setPositiveButton("OK",
									   new DialogInterface.OnClickListener() {
			                                 public void onClick(DialogInterface dialog, int whichButton) {			                                	 
			                                	 if (dropTag.equals(CardAttribute.MAINSTACK.toString())) {
			                     					if (!process.removeCardFromMainStack()) {
			                     						showAlert("card remove fail!!!");
			                     					}
			                     				} else if (dropTag.equals(CardAttribute.SIDESTACK.toString())) {

			                     					if (!process.removeCardFromSideStack()) {
			                     						showAlert("card remove fail!!!");
			                     					}
			                     				}
			                                	                         				
			                 			    	updateView();
			                                 }
			                           }
	    );	    
	    alertDialog.show();
	}
	
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		
	}
}
