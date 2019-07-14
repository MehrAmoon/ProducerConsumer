package sample.amoon.app.producerconsumerthreading;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button Producer, Consumer;
    ArrayList<CustomModel> ItemModelList;
    CustomAdapter customAdapter;
    int Counter = 0;
    Producer producer;
    Consumer consumer;

    final Handler handlerProducer = new Handler();
    final int delayProducer = 3000; //3000 milliseconds for each produce

    final Handler handlerConsumer = new Handler();
    final int delayConsumer = 4000; //4000 milliseconds for each consume

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        Producer = (Button) findViewById(R.id.producer);
        Consumer = (Button) findViewById(R.id.consumer);

        ItemModelList = new ArrayList<CustomModel>();
        customAdapter = new CustomAdapter(getApplicationContext(), ItemModelList);
        listView.setAdapter(customAdapter);

        //BlockingQueue implementations are designed to be used primarily for producer-consumer queues, but additionally support the Collection interface.
        BlockingQueue bq = new ArrayBlockingQueue(100000);
        producer = new Producer(bq);
        consumer = new Consumer(bq);

        //click Listener for Producer button
        Producer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a new thred for producer
                new Thread(producer).start();

            }
        });

        //click Listener for Producer button
        Consumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a new thred for consumer
                new Thread(consumer).start();

            }
        });


    }

    //method for adding new item to listview
    public void addValue() {
        Counter = Counter + 1;
        String name = String.valueOf(Counter);
        CustomModel md = new CustomModel(name);
        ItemModelList.add(md);
        customAdapter.notifyDataSetChanged();

    }

    //method for removing item from listview
    public void removeValue() {
        ItemModelList.remove(customAdapter.getCount() - 1);
        customAdapter.notifyDataSetChanged();
    }


    // method for create new producer
    class Producer implements Runnable {

        private BlockingQueue bq = null;
        public Producer(BlockingQueue queue) {
            this.setBlockingQueue(queue);
        }

        public void run() {
            //run a new handler that handler is part of the Android system's framework for managing threads. When you connect a Handler to your UI thread, the code that handles messages runs on the UI thread.
            handlerProducer.postDelayed(new Runnable() {
                public void run() {
                    try {
                        addValue();
                        bq.put(Counter);
//                        Log.println(Log.INFO,"produce","produced: " + bq.toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handlerProducer.postDelayed(this, delayProducer);
                }
            }, delayProducer);
        }

        public void setBlockingQueue(BlockingQueue bq) {
            this.bq = bq;
        }


    }

    // method for create new consumer
    class Consumer implements Runnable {

        protected BlockingQueue queue = null;

        public Consumer(BlockingQueue queue) {
            this.queue = queue;
        }

        public void run() {
            //run a new handler that handler is part of the Android system's framework for managing threads. When you connect a Handler to your UI thread, the code that handles messages runs on the UI thread.
            handlerConsumer.postDelayed(new Runnable() {
                public void run() {
                    try {
                        // check if queue is empty or not for consuming
                        if (queue.size()>0){
                            removeValue();
                            queue.take();
//                            Log.println(Log.INFO,"consume","Consumed: " + queue.take());
                        }
//                        else  Log.println(Log.INFO,"consume","dont have anything to consume");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handlerConsumer.postDelayed(this, delayConsumer);
                }
            }, delayConsumer);
        }
    }

}
