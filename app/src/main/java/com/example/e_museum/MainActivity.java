package com.example.e_museum;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.e_museum.databinding.ActivityMainBinding;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static SQLConnection sqlConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        runTask(() -> {
            try {
                //Background work here
//                String url = "jdbc:mysql://byyrm3ewrta8hnhxsoxm-mysql.services.clever-cloud.com:3306/byyrm3ewrta8hnhxsoxm";
//                String url = "jdbc:mysql://46.252.181.32:3306/byyrm3ewrta8hnhxsoxm";
//                String username = "urv00k6u73pbdhlv";
//                String password = "YsXWXThVUsLfNbaKL1aP";

                String url = "jdbc:mysql://b8fu1r5tflhnrnqjztht-mysql.services.clever-cloud.com:3306/b8fu1r5tflhnrnqjztht";
                String username = "unxmdvotjktefgp8";
                String password = "4XxtC2Ky5Dzz2AEEoC60";

                sqlConnection = new SQLConnection(url, username, password);

                sqlConnection.connectServer();

                Log.i("Database", "Connection established");

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public static void runTask(Runnable background, Runnable result, ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.setMessage("Đang tải");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        final CountDownLatch latch = new CountDownLatch(1);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                background.run();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null)
                            result.run();
                        latch.countDown();

                    }
                });

                try {
                    latch.await();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void runTask(Runnable background) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                background.run();
            }
        });
    }
}