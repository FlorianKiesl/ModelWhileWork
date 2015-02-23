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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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


public class ProcessFragment<TitlePageIndicator> extends Fragment implements DialogInterface.OnClickListener {

	final String MAINSTACK = "MAINSTACK", SIDESTACK = "SIDESTACK",
			MSGCARD = "MSGCARD", TASKCARD = "TASKCARD";
	
	private final int updateViewTime = 1000;
	private Handler updateViewHandler;
	private View fragment;
	private RelativeLayout rl_MainStack, rl_MainStackTaskCard, rl_MainStackMsgCard, rl_SideStack,
				           rl_SideStackTaskCard, rl_SideStackMsgCard, rl_TaskCard, rl_MsgCard;
	private CheckBox cb_Sender, cb_Reciver, cb_SenderMainStack, cb_ReciverMainStack, cb_SenderSideStack, cb_ReciverSideStack;
	private EditText te_MainStackTaskTitle, te_SideStackTaskTitle, te_MainStackMsgTitle, te_SideStackMsgTitle,
					 te_MainStackMsgPerson, te_SideStackMsgPerson, te_TaskTitle, te_MsgTitle, te_MsgSenderReciver;
	private TextView tv_Main, tv_Side, tv_processTitle;
	Process process;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		fragment = inflater.inflate(R.layout.fragment_process, container, false);
		
		updateViewHandler = new Handler();
		
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
			    	  updateViewNow();
		    	  }
		    			 
		      }
		  });
		
		cb_Reciver.setOnClickListener(new OnClickListener() {

		      @Override
		      public void onClick(View v) {
		    	  if(cb_Reciver.isChecked() == process.getMessageCard().isSender()) {
		    		  process.getMessageCard().setSender(!cb_Reciver.isChecked());
		    		  updateViewNow();
		    	  }
		      }
		  });

		cb_SenderMainStack.setOnClickListener(new OnClickListener() {

		      @Override
		      public void onClick(View v) { 
		    	  
		    	  if(cb_SenderMainStack.isChecked() != ((Message)process.getTopCardMainStack()).isSender()) {
		    		  ((Message)process.getTopCardMainStack()).setSender(cb_SenderMainStack.isChecked());
		    		  updateViewNow();
		    	  }
		      }
		  });
		
		cb_ReciverMainStack.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {
				 if(cb_ReciverMainStack.isChecked() == ((Message)process.getTopCardMainStack()).isSender()) {
					 ((Message)process.getTopCardMainStack()).setSender(!cb_ReciverMainStack.isChecked());
					 updateViewNow();
				 }
			 }
		  });
		
		cb_SenderSideStack.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) { 
				 if(cb_SenderSideStack.isChecked() != ((Message)process.getTopCardSideStack()).isSender()) {
					 ((Message)process.getTopCardSideStack()).setSender(cb_SenderSideStack.isChecked());
					 updateViewNow();
				 }				 
			 }
		  });
		
		cb_ReciverSideStack.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {
				 
				 if(cb_ReciverSideStack.isChecked() != ((Message)process.getTopCardSideStack()).isSender()) {
					 ((Message)process.getTopCardSideStack()).setSender(!cb_ReciverSideStack.isChecked());
					 updateViewNow();
				 }				 
			 }
		  });
		
		te_TaskTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				
				
				if(!te_TaskTitle.getText().toString().equals(process.getTaskCard().getTitle())) {
					process.getTaskCard().setTitle(te_TaskTitle.getText().toString());
					updateView();
				}
			}
		});
		
		te_MsgTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				
				
				if(!te_MsgTitle.getText().toString().equals(process.getMessageCard().getTitle())) {
					process.getMessageCard().setTitle(te_MsgTitle.getText().toString());
					updateView();
				}
			}
		});
		
		te_MsgSenderReciver.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				
				
				if(!te_MsgSenderReciver.getText().toString().equals(process.getMessageCard().getSenderReceiver())) {
					process.getMessageCard().setSenderReceiver(te_MsgSenderReciver.getText().toString());
					updateView();
				}
			}
		});
		
		te_MainStackTaskTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				
				if(process.getTopCardMainStack() != null) {
					if(!te_MainStackTaskTitle.getText().toString().equals(process.getTopCardMainStack().getTitle())) {
						process.getTopCardMainStack().setTitle(te_MainStackTaskTitle.getText().toString());
						updateView();
					}
				}
			}
		});
		
		te_SideStackTaskTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				
				if(process.getTopCardSideStack() != null) {
					if(!te_SideStackTaskTitle.getText().toString().equals(process.getTopCardSideStack().getTitle())) {
						process.getTopCardSideStack().setTitle(te_SideStackTaskTitle.getText().toString());
						updateView();
					}
				}				
			}
		});
		
		te_MainStackMsgTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				
				if(process.getTopCardMainStack() != null) {
					if(!te_MainStackMsgTitle.getText().toString().equals(process.getTopCardMainStack().getTitle())) {
						process.getTopCardMainStack().setTitle(te_MainStackMsgTitle.getText().toString());
						updateView();
					}					
				}
			}
		});
		
		te_SideStackMsgTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				
				if(process.getTopCardSideStack() != null) {
					if(!te_SideStackMsgTitle.getText().toString().equals(process.getTopCardSideStack().getTitle())) {
						process.getTopCardSideStack().setTitle(te_SideStackMsgTitle.getText().toString());
						updateView();
					}
				}				
			}
		});
		
		te_MainStackMsgPerson.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				
				if(process.getTopCardMainStack() != null) {
					if(!te_MainStackMsgPerson.getText().toString().equals(((Message)process.getTopCardMainStack()).getSenderReceiver())) {
						((Message)process.getTopCardMainStack()).setSenderReceiver(te_MainStackMsgPerson.getText().toString());
						updateView();
					}
				}				
			}	
		});
		
		te_SideStackMsgPerson.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) { 
				
				if(process.getTopCardSideStack() != null) {
					if(!te_SideStackMsgPerson.getText().toString().equals(((Message)process.getTopCardSideStack()).getSenderReceiver())) {
						((Message)process.getTopCardSideStack()).setSenderReceiver(te_SideStackMsgPerson.getText().toString());
						updateView();
					}
				}				
			}
		});				
		
		updateViewNow();
		
		return fragment;
	}
	
	public void onPause() {
		   super.onPause();	   
		   updateViewHandler.removeCallbacks(updateViewThread);
	};  

	public void onResume() {
		   super.onResume();	   
	};  
	
	private void addtOnClickListener4Card(ImageButton imgButtonContext, final Card card) {
	
		imgButtonContext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProcessFragment.this.fragment.getContext(), ContextInfoActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("CARD_ID", card.getTitle());
			    startActivity(intent);
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
						if ( !process.addCard(dataCard)) {
							showAlert("add card to stack fail!!!");
						}
						else {
							te_TaskTitle.setText("");
							te_MsgTitle.setText("");
							te_MsgSenderReciver.setText("");
							cb_Sender.setChecked(true);
							cb_Reciver.setChecked(false);
						}
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
				
				updateViewNow();

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
	
	private Runnable updateViewThread = new Runnable() {
	   	 
		   public void run() {
			   updateViewHandler.removeCallbacks(updateViewThread); //remove existing runnable
			   updateViewNow();			   
		   }
	 };

	private void updateView() {
		
		updateViewHandler.removeCallbacks(updateViewThread); //remove existing runnable
		updateViewHandler.postDelayed(updateViewThread, updateViewTime); //start new runnable
	}
	
	private void updateViewNow() {
		
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
                     	 updateViewNow();
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
			                                	                         				
			                                	updateViewNow();
			                                 }
			                           }
	    );	    
	    alertDialog.show();
	}
	
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
}
