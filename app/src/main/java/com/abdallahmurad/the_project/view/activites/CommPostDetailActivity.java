package com.abdallahmurad.the_project.view.activites;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abdallahmurad.the_project.R;
import com.abdallahmurad.the_project.model.Comment;
import com.abdallahmurad.the_project.model.Post;
import com.abdallahmurad.the_project.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AbdallahMurad on 8/3/2017.
 */

public class CommPostDetailActivity extends CommBaseActivity implements View.OnClickListener {
    public static final String TAG = "CommPostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private CommentAdapter mAdapter;
    private String mPostKey;

    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mBodyView;
    private EditText mCommentField;
    private Button mCommentButton;
    private RecyclerView mCommentsRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        initViews();
    }

    private void initViews() {
        mPostReference = FirebaseDatabase.getInstance().getReference().child("posts").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference().child("post-comments").child(mPostKey);

        mAuthorView = (TextView) findViewById(R.id.post_author);
        mTitleView = (TextView) findViewById(R.id.post_title);
        mBodyView = (TextView) findViewById(R.id.post_body);
        mCommentField = (EditText) findViewById(R.id.field_comment_text);
        mCommentButton = (Button) findViewById(R.id.button_post_comment);
        mCommentsRecycler = (RecyclerView) findViewById(R.id.recycler_comments);

        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);

                mAuthorView.setText(post.author);
                mTitleView.setText(post.title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(CommPostDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();

            }
        };
        mPostReference.addValueEventListener(postListener);
        mPostListener = postListener;
        mAdapter = new CommentAdapter(this, mCommentsReference);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
        mAdapter.cleanupListener();

    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView authorView;
        public TextView bodyView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            authorView = (TextView) itemView.findViewById(R.id.comment_author);
            bodyView = (TextView) itemView.findViewById(R.id.comment_body);
        }
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
        private Context context;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentsIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        public CommentAdapter(final Context mContext, DatabaseReference mDatabaseReference) {
            this.context = mContext;
            this.mDatabaseReference = mDatabaseReference;

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    Comment comment = dataSnapshot.getValue(Comment.class);

                    mCommentsIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentsIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        mComments.set(commentIndex, newComment);
                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentsIds.indexOf(commentKey);

                    if (commentIndex > -1) {
                        mComments.remove(commentIndex);
                        mCommentsIds.remove(commentIndex);
                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.v(TAG, "onChildRemoved:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    // FIXME: 9/3/2017
                    Toast.makeText(mContext, "Failed to load comments.", Toast.LENGTH_SHORT).show();
                }
            };
            mDatabaseReference.addChildEventListener(childEventListener);

            mChildEventListener = childEventListener;

        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Comment comment = mComments.get(position);
            holder.authorView.setText(comment.author);
            holder.bodyView.setText(comment.text);
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_post_comment) {
            postComment();
        }
    }

    private void postComment() {
        final String uid = getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String authorName = user.username;

                        String commentText = mCommentField.getText().toString();
                        Comment comment = new Comment(uid, authorName, commentText);

                        mCommentsReference.push().setValue(comment);

                        mCommentField.setText(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }}
