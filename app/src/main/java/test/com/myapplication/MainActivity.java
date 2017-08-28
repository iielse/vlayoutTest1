package test.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView vRecycler = (RecyclerView) findViewById(R.id.vRecycler);

        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(this);
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);


        vRecycler.setLayoutManager(virtualLayoutManager);
        vRecycler.setAdapter(delegateAdapter);


        ConcernAdapter concernAdapter = new ConcernAdapter();
        concernAdapter.set(Arrays.asList("a", "b", "c", "d"));
        delegateAdapter.addAdapter(concernAdapter);

        LoadMoreAdapter loadMoreAdapter = new LoadMoreAdapter();
        delegateAdapter.addAdapter(loadMoreAdapter);

        delegateAdapter.notifyDataSetChanged();
    }

    private static class ConcernAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {
        private final SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        private final List<String> dataList = new ArrayList<>();

        class ViewHolder extends RecyclerView.ViewHolder {
            int pos;
            String val;

            TextView vValue;
            TextView vDel;

            ViewHolder(View itemView) {
                super(itemView);
                vValue = (TextView) itemView.findViewById(R.id.vValue);
                vDel = (TextView) itemView.findViewById(R.id.vDel);
                vDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataList.remove(val);
                        notifyDataSetChanged();
                    }
                });
            }

            void refresh(String value, int position) {
                val = value;
                pos = position;

                vValue.setText("value :" + value);
            }
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return singleLayoutHelper;
        }

        public void set(List<String> sourceList) {
            dataList.clear();
            dataList.addAll(sourceList);
            notifyDataSetChanged();
        }

        public void add(List<String> sourceList) {
            dataList.addAll(sourceList);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_content, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).refresh(dataList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }


    private static class LoadMoreAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {
        private final SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(View itemView) {
                super(itemView);
            }

            void refresh() {
            }
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return singleLayoutHelper;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_load_more, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).refresh();
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }
}
