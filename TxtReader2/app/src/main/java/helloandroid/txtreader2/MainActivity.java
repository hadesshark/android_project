package helloandroid.txtreader2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String bookList_str[];
    private CardView cardview;
    private FloatingActionButton faBtn_up, faBtn_down;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {

        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final List<Book> bookList = new ArrayList<>();
        try {
            bookList_str = getAssets().list("Bookstore");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String book_name : bookList_str) {
            System.out.println(book_name);
            bookList.add(new Book(book_name));
        }

        recyclerView.setAdapter(new BookAdapter(bookList));
        cardview = (CardView) findViewById(R.id.card_view);

        faBtn_up = (FloatingActionButton) findViewById(R.id.faBtn_up);
        faBtn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        faBtn_down = (FloatingActionButton) findViewById(R.id.faBtn_down);
        faBtn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(recyclerView.getBottom());
            }
        });

    }

    class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

        private List<Book> bookList;

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvName;
            private CardView cardView;

            public ViewHolder(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                cardView = (CardView) itemView.findViewById(R.id.card_view);
            }
        }

        public BookAdapter(List<Book> bookList) {
            this.bookList = bookList;
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reader_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Book book = bookList.get(position);
            holder.tvName.setText(book.getBook_name());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("fileName", book.getBook_name());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
}
