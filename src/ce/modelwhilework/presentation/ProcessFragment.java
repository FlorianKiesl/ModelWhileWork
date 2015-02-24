package ce.modelwhilework.presentation;

import ce.modelwhilework.data.Card;
import ce.modelwhilework.data.Message;
import ce.modelwhilework.data.Process;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.Task;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ProcessFragment extends Fragment implements DialogInterface.OnClickListener {

	final String MAINSTACK = "MAINSTACK", SIDESTACK = "SIDESTACK",
			MSGCARD = "MSGCARD", TASKCARD = "TASKCARD";
	
	private View fragment;
	private RelativeLayout rl_MainStack, rl_MainStackTaskCard, rl_MainStackMsgCard, rl_SideStack,
				           rl_SideStackTaskCard, rl_SideStackMsgCard, rl_TaskCard, rl_MsgCard;
	private CheckBox cb_Sender, cb_Reciver, cb_SenderMainStack, cb_ReciverMainStack, cb_SenderSideStack, cb_ReciverSideStack;
	private EditText te_MainStackTaskTitle, te_SideStackTaskTitle, te_MainStackMsgTitle, te_SideStackMsgTitle,
					 te_MainStackMsgPerson, te_SideStackMsgPerson, te_TaskTitle, te_MsgTitle, te_MsgSenderReciver;
	private TextView tv_Main, tv_Side, tv_processTitle;
	private boolean keyboardVisible = false;
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
		
		ImageButton imgButtonContext = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoProcess);
		imgButtonContext.setOnClickListener(new View.OnClickListener() {
			
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
		
		imgButtonContext = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardMsg_MainStack);		
		addtOnClickListener4Card(imgButtonContext, process.getTopCardMainStack());
		
		imgButtonContext = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardTask_MainStack);
		addtOnClickListener4Card(imgButtonContext, process.getTopCardMainStack());
		
		imgButtonContext = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardMsg_SideStack);
		addtOnClickListener4Card(imgButtonContext, process.getTopCardSideStack());
		
		imgButtonContext = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardTask_SideStack);
		addtOnClickListener4Card(imgButtonContext, process.getTopCardSideStack());
		
		imgButtonContext = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardMsg);
		addtOnClickListener4Card(imgButtonContext, process.getMessageCard());
		
		imgButtonContext = (ImageButton) fragment.findViewById(R.id.fragment_process_imageButton_ContextInfoCardTask);
		addtOnClickListener4Card(imgButtonContext, process.getTaskCard());
		
		tv_Main = (TextView) fragment.findViewById(R.id.textViewMainStack);
		tv_Side = (TextView) fragment.findViewById(R.id.textViewSideStack);
		tv_processTitle = (TextView) fragment.findViewById(R.id.textViewProcessTitle);	
		
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
		
		cb_Sender.setOnClickListener(new OnClickListener() {

		      @Override
		      public void onClick(View v) {
		    	  if(cb_Sender.isChecked() != process.getMessageCard().isSender()) {
		    		  process.getMessageCard().setSender(cb_Sender.isChecked());
		    		  process.storeXML(ProcessManager.getInstance().getInternalStoreage());
			    	  updateView();
		    	  }
		    			 
		      }
		  });
		
		cb_Reciver.setOnClickListener(new OnClickListener() {

		      @Override
		      public void onClick(View v) {
		    	  if(cb_Reciver.isChecked() == process.getMessageCard().isSender()) {
		    		  process.getMessageCard().setSender(!cb_Reciver.isChecked());
		    		  process.storeXML(ProcessManager.getInstance().getInternalStoreage());
			    	  updateView();
		    	  }
		      }
		  });

		cb_SenderMainStack.setOnClickListener(new OnClickListener() {

		      @Override
		      public void onClick(View v) { 
		    	  
		    	  if(process.getTopCardMainStack() != null) {
		    		  if(cb_SenderMainStack.isChecked() != ((Message)process.getTopCardMainStack()).isSender()) {
			    		  ((Message)process.getTopCardMainStack()).setSender(cb_SenderMainStack.isChecked());
			    		  process.storeXML(ProcessManager.getInstance().getInternalStoreage());
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
						 process.storeXML(ProcessManager.getInstance().getInternalStoreage());
				    	  updateView();
					 }
				 }
			 }
		  });
		
		cb_SenderSideStack.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) { 
				 
				 if(process.getTopCardSideStack() != null) {
					 if(cb_SenderSideStack.isChecked() != ((Message)process.getTopCardSideStack()).isSender()) {
						 ((Message)process.getTopCardSideStack()).setSender(cb_SenderSideStack.isChecked());
						 process.storeXML(ProcessManager.getInstance().getInternalStoreage());
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
					 process.storeXML(ProcessManager.getInstance().getInternalStoreage());
			    	  updateView();
				 }				 
			 }
		  });

		te_TaskTitle.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus) {
					
					process.getTaskCard().setTitle(te_TaskTitle.getText().toString());
					process.storeXML(ProcessManager.getInstance().getInternalStoreage());
			    	updateView();
				}				
			}
		}); 
		
		te_MsgTitle.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus) {
					
					process.getMessageCard().setTitle(te_MsgTitle.getText().toString());
					process.storeXML(ProcessManager.getInstance().getInternalStoreage());
			    	updateView();
				}				
			}
		}); 
		
		
		te_MsgSenderReciver.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus) {
				
					process.getMessageCard().setSenderReceiver(te_MsgSenderReciver.getText().toString());
					process.storeXML(ProcessManager.getInstance().getInternalStoreage());
			    	updateView();
				}				
			}
		}); 
		
		te_MainStackTaskTitle.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus) {
					
					if(process.getTopCardMainStack() instanceof Task) {
						
						if(te_MainStackTaskTitle.getText().length() > 0)
							process.getTopCardMainStack().setTitle(te_MainStackTaskTitle.getText().toString());
						else
							showAlert("You have to enter a valid title!!!");
						
						process.storeXML(ProcessManager.getInstance().getInternalStoreage());
				    	updateView();
					}
				}				
			}
		}); 
		
		te_SideStackTaskTitle.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus) {
					
					if(process.getTopCardSideStack() instanceof Task) {
					
						if(te_SideStackTaskTitle.getText().length() > 0)
							process.getTopCardSideStack().setTitle(te_SideStackTaskTitle.getText().toString());
						else
							showAlert("You have to enter a valid title!!!");
						
						process.storeXML(ProcessManager.getInstance().getInternalStoreage());
				    	updateView();
					}					
				}				
			}
		}); 
		
		te_MainStackMsgTitle.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus) {
					
					if(process.getTopCardMainStack() instanceof Message) {
						if(te_MainStackMsgTitle.getText().length() > 0)
							process.getTopCardMainStack().setTitle(te_MainStackMsgTitle.getText().toString());
						else
							showAlert("You have to enter a valid title!!!");
						
						process.storeXML(ProcessManager.getInstance().getInternalStoreage());
				    	updateView();
					}
				}				
			}
		}); 
		
		te_SideStackMsgTitle.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus) {
					
					if(process.getTopCardSideStack() instanceof Message) {
						if(te_SideStackMsgTitle.getText().length() > 0)
							process.getTopCardSideStack().setTitle(te_SideStackMsgTitle.getText().toString());
						else
							showAlert("You have to enter a valid title!!!");
						
						process.storeXML(ProcessManager.getInstance().getInternalStoreage());
				    	updateView();
					}					
				}				
			}
		}); 
		
		te_MainStackMsgPerson.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus) {
					
					if(process.getTopCardMainStack() instanceof Message) {
						Message m = (Message)process.getTopCardMainStack();
						if(!te_MainStackMsgPerson.getText().toString().equals(m.getSenderReceiver())) {
							
							if(te_MainStackMsgPerson.getText().length() > 0)
								m.setSenderReceiver(te_MainStackMsgPerson.getText().toString());
							else
								showAlert("You have to enter a sender/receiver!!!");
							
						}
						process.storeXML(ProcessManager.getInstance().getInternalStoreage());
				    	updateView();
					}
				}				
			}
		});
		
		te_SideStackMsgPerson.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus) {
					
					if(process.getTopCardSideStack() instanceof Message) {
						Message m = (Message)process.getTopCardSideStack();
						if(!te_SideStackMsgPerson.getText().toString().equals(m.getSenderReceiver())) {
							if(te_SideStackMsgPerson.getText().length() > 0)
								m.setSenderReceiver(te_SideStackMsgPerson.getText().toString());
							else
								showAlert("You have to enter a sender/receiver!!!");
							
						}
						process.storeXML(ProcessManager.getInstance().getInternalStoreage());
				    	updateView();
					}	
				}				
			}
		}); 
		
		return fragment;
	}
	
	public void onPause() {
		   super.onPause();	   
		   process.storeXML(ProcessManager.getInstance().getInternalStoreage());
	};  

	public void onResume() {
		   super.onResume();	   
		   updateView();
	};  
	
	private void addtOnClickListener4Card(ImageButton imgButtonContext, final Card card) {
	
		imgButtonContext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(card.getTitle().length() > 0) {
					Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), ContextInfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CARD_ID", card.getTitle());
				    startActivity(intent);
				}
				else
					showAlert("Please enter a valid title!!!");
				
			}
		});
	}
	
	
	private final class ChoiceTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
	
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);

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
					Card dataCard = null;
					String title = "";
					if (dropTag.equals(MSGCARD)) {			
						
						title = te_MsgTitle.getText().toString();
						String senderReciver = te_MsgSenderReciver.getText().toString();
						boolean sender = cb_Sender.isChecked();
						boolean reciver = cb_Reciver.isChecked();
												
						if(title.length() == 0) {
							showAlert("card has no title!");
						}
						else if(!sender && !reciver) {
							showAlert("please select a sender or a reciver!");
						}
						else if(senderReciver.length() == 0) {
							if(sender) { showAlert("card has no sender!"); }
							else { showAlert("card has no reciver!"); }
						}
						else
							dataCard = new Message(title, senderReciver, sender);						
					} else {
						
						title = te_TaskTitle.getText().toString();
						
						if(title.length() == 0) {
							showAlert("card has no title!");
						}
						else
							dataCard = new Task(title);			
					}

					if(dataCard != null) {
						if (process.addCard(dataCard)) {
							
							if(dataCard instanceof Message)
								process.setMessageCard(new Message("", "", true));
							else if(dataCard instanceof Task)
								process.setTaskCard(new Task(""));
						}
						else
							showAlert("add card to stack fail!!!");
					}

				} else if (dropTag.equals(MAINSTACK)
						&& targetTag.equals(SIDESTACK)) {

					// card from main stack --> move it to side stack
					if (!process.putCardAside()) {
						showAlert("move card fail!!!");
					}
				} else if (dropTag.equals(SIDESTACK)
						&& targetTag.equals(MAINSTACK)) {

					// card from main stack --> move it to side stack
					if (!process.putBackFromAside()) {
						showAlert("move card fail!!!");
					}
				}
				
				process.storeXML(ProcessManager.getInstance().getInternalStoreage());
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

				//set question: do you really want to delete the card?
				String dropTag = (String) dropElement.getTag();
				showDeleteQuestion("Are you positive to delete the card?", dropTag);

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
		
		tv_processTitle.setText(process.getTitle());

		// display main and side stack		
		Card card = process.getTopCardMainStack();
		if (card != null) {

			if(card instanceof Message) {
				rl_MainStackTaskCard.setVisibility(View.INVISIBLE);
				rl_MainStackMsgCard.setVisibility(View.VISIBLE);
				tv_Main.setVisibility(View.INVISIBLE);			
				
				te_MainStackMsgTitle.setText(card.getTitle());
				te_MainStackMsgTitle.setSelection(te_MainStackMsgTitle.getText().length());
				te_MainStackMsgPerson.setText(((Message)card).getSenderReceiver());
				te_MainStackMsgPerson.setSelection(te_MainStackMsgPerson.getText().length());
				cb_SenderMainStack.setChecked(((Message)card).isSender());
				cb_ReciverMainStack.setChecked(!((Message)card).isSender());				
			} else if(card instanceof Task) {
				rl_MainStackTaskCard.setVisibility(View.VISIBLE);
				rl_MainStackMsgCard.setVisibility(View.INVISIBLE);
				tv_Main.setVisibility(View.INVISIBLE);
				te_MainStackTaskTitle.setText(card.getTitle());		
				te_MainStackTaskTitle.setSelection(te_MainStackTaskTitle.getText().length());
			} else {
				showAlert("invalid card type!!!");
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

			if(card instanceof Message) {
				rl_SideStackTaskCard.setVisibility(View.INVISIBLE);
				rl_SideStackMsgCard.setVisibility(View.VISIBLE);
				tv_Side.setVisibility(View.INVISIBLE);
				
				te_SideStackMsgTitle.setText(card.getTitle());
				te_SideStackMsgTitle.setSelection(te_SideStackMsgTitle.getText().length());
				te_SideStackMsgPerson.setText(((Message)card).getSenderReceiver());
				te_SideStackMsgPerson.setSelection(te_SideStackMsgPerson.getText().length());
				cb_SenderSideStack.setChecked(((Message)card).isSender());
				cb_ReciverSideStack.setChecked(!((Message)card).isSender());				
			} else if(card instanceof Task) {
				rl_SideStackTaskCard.setVisibility(View.VISIBLE);
				rl_SideStackMsgCard.setVisibility(View.INVISIBLE);
				tv_Side.setVisibility(View.INVISIBLE);
				te_SideStackTaskTitle.setText(card.getTitle());		
				te_SideStackTaskTitle.setSelection(te_SideStackTaskTitle.getText().length());
			} else {
				showAlert("invalid card type!!!");
			}
			
			rl_SideStack.setOnTouchListener(new ChoiceTouchListener());	
			
		} else {

			rl_SideStackTaskCard.setVisibility(View.INVISIBLE);
			rl_SideStackMsgCard.setVisibility(View.INVISIBLE);
			tv_Side.setVisibility(View.VISIBLE);			
			rl_SideStack.setOnTouchListener(new DoNothingTouchListener());
		}
		
		Task task = process.getTaskCard();
		if (task != null) {
			te_TaskTitle.setText(task.getTitle());
			te_TaskTitle.setSelection(te_TaskTitle.getText().length());
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
		} else {
			showAlert("general error update view!!!");
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
                    	process.storeXML(ProcessManager.getInstance().getInternalStoreage());
      			    	updateView();
                      }
                }
		);
		alertDialog.setPositiveButton("OK",
									   new DialogInterface.OnClickListener() {
			                                 public void onClick(DialogInterface dialog, int whichButton) {			                                	 
			                                	 if (dropTag.equals(MAINSTACK)) {
			                     					if (!process.removeCardFromMainStack()) {
			                     						showAlert("card remove fail!!!");
			                     					}
			                     				} else if (dropTag.equals(SIDESTACK)) {

			                     					if (!process.removeCardFromSideStack()) {
			                     						showAlert("card remove fail!!!");
			                     					}
			                     				}
			                                	                         				
			                                	process.storeXML(ProcessManager.getInstance().getInternalStoreage());
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
