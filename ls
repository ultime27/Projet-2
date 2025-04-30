[33mcommit fe087f6ad91b0ce80165b62f121d3c74534251e0[m
Author: MatthieusSuchet <matthieu.suchet@outlook.fr>
Date:   Mon Apr 28 23:48:14 2025 -0700

    almostFinishAddStock

[1mdiff --git a/app/src/main/AndroidManifest.xml b/app/src/main/AndroidManifest.xml[m
[1mindex a1482bd..2f6f28e 100644[m
[1m--- a/app/src/main/AndroidManifest.xml[m
[1m+++ b/app/src/main/AndroidManifest.xml[m
[36m@@ -12,6 +12,12 @@[m
         android:supportsRtl="true"[m
         android:theme="@style/Theme.AppCompat.DayNight.DarkActionBar"[m
         tools:targetApi="31">[m
[32m+[m[32m        <activity[m
[32m+[m[32m            android:name=".database.stocks.AddStockActivity"[m
[32m+[m[32m            android:exported="false" />[m
[32m+[m[32m        <activity[m
[32m+[m[32m            android:name=".database.stocks.StockActivity"[m
[32m+[m[32m            android:exported="false" />[m
         <activity[m
             android:name=".Calendar_activity"[m
             android:exported="false" />[m
[1mdiff --git a/app/src/main/java/com/suchet/smartFridge/LandingPage.java b/app/src/main/java/com/suchet/smartFridge/LandingPage.java[m
[1mindex e7457d0..fa1fbae 100644[m
[1m--- a/app/src/main/java/com/suchet/smartFridge/LandingPage.java[m
[1m+++ b/app/src/main/java/com/suchet/smartFridge/LandingPage.java[m
[36m@@ -18,6 +18,7 @@[m [mimport androidx.lifecycle.LiveData;[m
 [m
 import com.suchet.smartFridge.database.SmartFridgeRepository;[m
 import com.suchet.smartFridge.database.entities.User;[m
[32m+[m[32mimport com.suchet.smartFridge.database.stocks.StockActivity;[m
 import com.suchet.smartFridge.databinding.ActivityLandingPageBinding;[m
 [m
 [m
[36m@@ -50,6 +51,7 @@[m [mpublic class LandingPage extends AppCompatActivity {[m
         updateSharedPreference();[m
         logoutButton();[m
         goToCalendarButton();[m
[32m+[m[32m        goToStockButton();[m
     }[m
 [m
     private void loginUser(Bundle savedInstanceState) {[m
[36m@@ -182,4 +184,13 @@[m [mpublic class LandingPage extends AppCompatActivity {[m
         });[m
     }[m
 [m
[32m+[m[32m    private void goToStockButton(){[m
[32m+[m[32m        binding.GoToStockActivity.setOnClickListener(new View.OnClickListener() {[m
[32m+[m[32m            @Override[m
[32m+[m[32m            public void onClick(View v) {[m
[32m+[m[32m                startActivity(StockActivity.StockIntentFactory(getApplicationContext()));[m
[32m+[m[32m            }[m
[32m+[m[32m        });[m
[32m+[m[32m    }[m
[32m+[m
 }[m
\ No newline at end of file[m
[1mdiff --git a/app/src/main/java/com/suchet/smartFridge/database/StockDatabase.java b/app/src/main/java/com/suchet/smartFridge/database/StockDatabase.java[m
[1mnew file mode 100644[m
[1mindex 0000000..90697a3[m
[1m--- /dev/null[m
[1m+++ b/app/src/main/java/com/suchet/smartFridge/database/StockDatabase.java[m
[36m@@ -0,0 +1,17 @@[m
[32m+[m[32mpackage com.suchet.smartFridge.database;[m
[32m+[m
[32m+[m[32mimport androidx.room.Database;[m
[32m+[m[32mimport androidx.room.RoomDatabase;[m
[32m+[m[32mimport androidx.room.TypeConverters;[m
[32m+[m
[32m+[m[32mimport com.suchet.smartFridge.database.TypeConverter.TypeLocalDateConverter;[m
[32m+[m[32mimport com.suchet.smartFridge.database.entities.Food;[m
[32m+[m
[32m+[m[32m@Database(entities = {Food.class},version = 1,exportSchema = false)[m
[32m+[m[32m@TypeConverters(TypeLocalDateConverter.class)[m
[32m+[m[32mpublic abstract class StockDatabase extends RoomDatabase {[m
[32m+[m[32m    public abstract FoodDAO foodDAO();[m
[32m+[m
[32m+[m
[32m+[m[32m}[m
[32m+[m
[1mdiff --git a/app/src/main/java/com/suchet/smartFridge/database/entities/Food.java b/app/src/main/java/com/suchet/smartFridge/database/entities/Food.java[m
[1mindex 96c9543..5be0b91 100644[m
[1m--- a/app/src/main/java/com/suchet/smartFridge/database/entities/Food.java[m
[1m+++ b/app/src/main/java/com/suchet/smartFridge/database/entities/Food.java[m
[36m@@ -7,10 +7,19 @@[m [mimport java.time.LocalDate;[m
 @Entity[m
 public class Food {[m
     @PrimaryKey(autoGenerate = true)[m
[32m+[m[32m    private int id;[m
     private String name;[m
     private double quantity;[m
     private LocalDate datePeremption;[m
 [m
[32m+[m[32m    public int getId() {[m
[32m+[m[32m        return id;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public void setId(int id) {[m
[32m+[m[32m        this.id = id;[m
[32m+[m[32m    }[m
[32m+[m
     public Food(String name) {[m
         this.name = name;[m
         quantity=0.0;[m
[1mdiff --git a/app/src/main/java/com/suchet/smartFridge/database/stocks/AddStockActivity.java b/app/src/main/java/com/suchet/smartFridge/database/stocks/AddStockActivity.java[m
[1mnew file mode 100644[m
[1mindex 0000000..c24a6e1[m
[1m--- /dev/null[m
[1m+++ b/app/src/main/java/com/suchet/smartFridge/database/stocks/AddStockActivity.java[m
[36m@@ -0,0 +1,56 @@[m
[32m+[m[32mpackage com.suchet.smartFridge.database.stocks;[m
[32m+[m
[32m+[m[32mimport android.content.Context;[m
[32m+[m[32mimport android.content.Intent;[m
[32m+[m[32mimport android.os.Bundle;[m
[32m+[m[32mimport android.view.View;[m
[32m+[m
[32m+[m[32mimport androidx.activity.EdgeToEdge;[m
[32m+[m[32mimport androidx.appcompat.app.AppCompatActivity;[m
[32m+[m[32mimport androidx.core.graphics.Insets;[m
[32m+[m[32mimport androidx.core.view.ViewCompat;[m
[32m+[m[32mimport androidx.core.view.WindowInsetsCompat;[m
[32m+[m
[32m+[m[32mimport com.suchet.smartFridge.LoginActivity;[m
[32m+[m[32mimport com.suchet.smartFridge.R;[m
[32m+[m[32mimport com.suchet.smartFridge.database.entities.Food;[m
[32m+[m[32mimport com.suchet.smartFridge.databinding.ActivityAddStockBinding;[m
[32m+[m[32mimport com.suchet.smartFridge.databinding.ActivityStockBinding;[m
[32m+[m
[32m+[m[32mpublic class AddStockActivity extends AppCompatActivity {[m
[32m+[m
[32m+[m[32m    private ActivityAddStockBinding binding;[m
[32m+[m[32m    private String name;[m
[32m+[m[32m    private int count;[m
[32m+[m
[32m+[m[32m    @Override[m
[32m+[m[32m    protected void onCreate(Bundle savedInstanceState) {[m
[32m+[m[32m        super.onCreate(savedInstanceState);[m
[32m+[m[32m        binding = ActivityAddStockBinding.inflate(getLayoutInflater());[m
[32m+[m[32m        setContentView(binding.getRoot());[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private void addToStock() {[m
[32m+[m[32m        String name = binding.foodNameEditText.getText().toString().trim();[m
[32m+[m[32m        String countStr = binding.foodAmountEditText.getText().toString().trim();[m
[32m+[m
[32m+[m[32m        if (name.isEmpty() || countStr.isEmpty()) {[m
[32m+[m[32m            return;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        int count = Integer.parseInt(countStr);[m
[32m+[m[32m        binding.addToTheStockButton.setOnClickListener(new View.OnClickListener() {[m
[32m+[m[32m            @Override[m
[32m+[m[32m            public void onClick(View v) {[m
[32m+[m[32m                Food food = new Food(name);[m
[32m+[m[32m                food.setQuantity(count);[m
[32m+[m[32m                StockActivity.addFoodToStock(getApplicationContext(), food);[m
[32m+[m[32m            }[m
[32m+[m[32m        });[m
[32m+[m[32m    }[m
[32m+[m[32m    static Intent AddToStockIntentFactory(Context context) {[m
[32m+[m[32m        return new Intent(context, AddStockActivity.class);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    //TODO : demander comment on peut verif que ca add a la db[m
[32m+[m[32m}[m
\ No newline at end of file[m
[1mdiff --git a/app/src/main/java/com/suchet/smartFridge/database/stocks/StockActivity.java b/app/src/main/java/com/suchet/smartFridge/database/stocks/StockActivity.java[m
[1mnew file mode 100644[m
[1mindex 0000000..2377e72[m
[1m--- /dev/null[m
[1m+++ b/app/src/main/java/com/suchet/smartFridge/database/stocks/StockActivity.java[m
[36m@@ -0,0 +1,71 @@[m
[32m+[m[32mpackage com.suchet.smartFridge.database.stocks;[m
[32m+[m
[32m+[m[32mimport android.content.Context;[m
[32m+[m[32mimport android.content.Intent;[m
[32m+[m[32mimport android.os.Bundle;[m
[32m+[m[32mimport android.view.View;[m
[32m+[m
[32m+[m[32mimport androidx.appcompat.app.AppCompatActivity;[m
[32m+[m
[32m+[m
[32m+[m[32mimport com.suchet.smartFridge.database.StockDatabase;[m
[32m+[m[32mimport com.suchet.smartFridge.database.entities.Food;[m
[32m+[m[32mimport com.suchet.smartFridge.databinding.ActivityStockBinding;[m
[32m+[m
[32m+[m[32mimport java.util.List;[m
[32m+[m
[32m+[m[32mpublic class StockActivity extends AppCompatActivity {[m
[32m+[m[32m    private ActivityStockBinding binding;[m
[32m+[m
[32m+[m[32m    @Override[m
[32m+[m[32m    protected void onCreate(Bundle savedInstanceState) {[m
[32m+[m[32m        super.onCreate(savedInstanceState);[m
[32m+[m[32m        binding = ActivityStockBinding.inflate(getLayoutInflater());[m
[32m+[m[32m        setContentView(binding.getRoot());[m
[32m+[m
[32m+[m[32m        GoToAddStockActivity();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private void GoToAddStockActivity() {[m
[32m+[m[32m        binding.addFoodInStockButton.setOnClickListener(new View.OnClickListener() {[m
[32m+[m[32m            @Override[m
[32m+[m[32m            public void onClick(View v) {[m
[32m+[m[32m                startActivity(AddStockActivity.AddToStockIntentFactory(getApplicationContext()));[m
[32m+[m[32m            }[m
[32m+[m[32m        });[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private void addButton() {[m
[32m+[m[32m        binding.addFoodInStockButton.setOnClickListener(new View.OnClickListener() {[m
[32m+[m[32m            @Override[m
[32m+[m[32m            public void onClick(View v) {[m
[32m+[m[32m                Intent intent = AddStockActivity.AddToStockIntentFactory(getApplicationContext());[m
[32m+[m[32m                startActivity(intent);[m
[32m+[m[32m            }[m
[32m+[m[32m        });[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public static String getStock(Context context){[m
[32m+[m[32m        StockDatabase stockDatabase = StockClient.getInstance(context).getStockDatabase();[m
[32m+[m[32m        List<Food> list = stockDatabase.foodDAO().getAllFoods();[m
[32m+[m[32m        StringBuilder stock = new StringBuilder();[m
[32m+[m[32m        for (Food food : list) {[m
[32m+[m[32m            stock.append(food.getName()).append(" - ").append(food.getQuantity()).append("\n");[m
[32m+[m[32m        }[m
[32m+[m[32m        return stock.toString();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public static void addFoodToStock(Context context, Food food) {[m
[32m+[m[32m        StockDatabase stockDatabase = StockClient.getInstance(context).getStockDatabase();[m
[32m+[m[32m        stockDatabase.foodDAO().insert(food);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public static void deleteFoodToStock(Context context, Food food) {[m
[32m+[m[32m        StockDatabase stockDatabase = StockClient.getInstance(context).getStockDatabase();[m
[32m+[m[32m        stockDatabase.foodDAO().delete(food);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public static Intent StockIntentFactory(Context context) {[m
[32m+[m[32m        return new Intent(context, StockActivity.class);[m
[32m+[m[32m    }[m
[32m+[m[32m}[m
\ No newline at end of file[m
[1mdiff --git a/app/src/main/java/com/suchet/smartFridge/database/stocks/StockClient.java b/app/src/main/java/com/suchet/smartFridge/database/stocks/StockClient.java[m
[1mnew file mode 100644[m
[1mindex 0000000..6074df3[m
[1m--- /dev/null[m
[1m+++ b/app/src/main/java/com/suchet/smartFridge/database/stocks/StockClient.java[m
[36m@@ -0,0 +1,33 @@[m
[32m+[m[32mpackage com.suchet.smartFridge.database.stocks;[m
[32m+[m
[32m+[m
[32m+[m[32mimport android.content.Context;[m
[32m+[m[32mimport androidx.room.Room;[m
[32m+[m
[32m+[m[32mimport com.suchet.smartFridge.database.StockDatabase;[m
[32m+[m
[32m+[m[32mpublic class StockClient {[m
[32m+[m
[32m+[m[32m    private static StockClient instance;[m
[32m+[m[32m    private final StockDatabase stockDatabase;[m
[32m+[m
[32m+[m[32m    private StockClient(Context context) {[m
[32m+[m[32m        stockDatabase = Room.databaseBuilder([m
[32m+[m[32m                        context.getApplicationContext(),[m
[32m+[m[32m                        StockDatabase.class,[m
[32m+[m[32m                        "StockDatabase"[m
[32m+[m[32m                ).fallbackToDestructiveMigration()[m
[32m+[m[32m                .build();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public static synchronized StockClient getInstance(Context context) {[m
[32m+[m[32m        if (instance == null) {[m
[32m+[m[32m            instance = new StockClient(context);[m
[32m+[m[32m        }[m
[32m+[m[32m        return instance;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public StockDatabase getStockDatabase() {[m
[32m+[m[32m        return stockDatabase;[m
[32m+[m[32m    }[m
[32m+[m[32m}[m
[1mdiff --git a/app/src/main/res/layout/activity_add_stock.xml b/app/src/main/res/layout/activity_add_stock.xml[m
[1mnew file mode 100644[m
[1mindex 0000000..e0c66fd[m
[1m--- /dev/null[m
[1m+++ b/app/src/main/res/layout/activity_add_stock.xml[m
[36m@@ -0,0 +1,41 @@[m
[32m+[m[32m<?xml version="1.0" encoding="utf-8"?>[m
[32m+[m[32m<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"[m
[32m+[m[32m    xmlns:app="http://schemas.android.com/apk/res-auto"[m
[32m+[m[32m    xmlns:tools="http://schemas.android.com/tools"[m
[32m+[m[32m    android:id="@+id/main"[m
[32m+[m[32m    android:layout_width="match_parent"[m
[32m+[m[32m    android:layout_height="match_parent"[m
[32m+[m[32m    tools:context=".database.stocks.AddStockActivity">[m
[32m+[m
[32m+[m
[32m+[m[32m    <EditText[m
[32m+[m[32m        android:id="@+id/foodNameEditText"[m
[32m+[m[32m        android:layout_width="300dp"[m
[32m+[m[32m        android:layout_height="50dp"[m
[32m+[m[32m        android:layout_marginTop="100dp"[m
[32m+[m[32m        android:hint="@string/enter_food_s_name"[m
[32m+[m[32m        app:layout_constraintEnd_toEndOf="parent"[m
[32m+[m[32m        app:layout_constraintStart_toStartOf="parent"[m
[32m+[m[32m        app:layout_constraintTop_toTopOf="parent" />[m
[32m+[m
[32m+[m[32m    <EditText[m
[32m+[m[32m        android:id="@+id/foodAmountEditText"[m
[32m+[m[32m        android:layout_width="300dp"[m
[32m+[m[32m        android:layout_height="50dp"[m
[32m+[m[32m        android:hint="@string/enter_the_amount"[m
[32m+[m[32m        app:layout_constraintEnd_toEndOf="parent"[m
[32m+[m[32m        app:layout_constraintStart_toStartOf="parent"[m
[32m+[m[32m        app:layout_constraintTop_toBottomOf="@+id/foodNameEditText"[m
[32m+[m[32m        />[m
[32m+[m
[32m+[m[32m    <Button[m
[32m+[m[32m        android:id="@+id/addToTheStockButton"[m
[32m+[m[32m        android:layout_width="wrap_content"[m
[32m+[m[32m        android:layout_height="wrap_content"[m
[32m+[m[32m        android:layout_marginTop="140dp"[m
[32m+[m[32m        android:text="@string/addStock"[m
[32m+[m[32m        app:layout_constraintEnd_toEndOf="parent"[m
[32m+[m[32m        app:layout_constraintStart_toStartOf="parent"[m
[32m+[m[32m        app:layout_constraintTop_toBottomOf="@+id/foodAmountEditText" />[m
[32m+[m
[32m+[m[32m</androidx.constraintlayout.widget.ConstraintLayout>[m
\ No newline at end of file[m
[1mdiff --git a/app/src/main/res/layout/activity_landing_page.xml b/app/src/main/res/layout/activity_landing_page.xml[m
[1mindex 11090af..330f178 100644[m
[1m--- a/app/src/main/res/layout/activity_landing_page.xml[m
[1m+++ b/app/src/main/res/layout/activity_landing_page.xml[m
[36m@@ -57,4 +57,14 @@[m
         app:layout_constraintEnd_toEndOf="parent"[m
         app:layout_constraintStart_toStartOf="parent"[m
         app:layout_constraintTop_toTopOf="parent" />[m
[32m+[m
[32m+[m[32m    <Button[m
[32m+[m[32m        android:id="@+id/GoToStockActivity"[m
[32m+[m[32m        android:layout_width="wrap_content"[m
[32m+[m[32m        android:layout_height="wrap_content"[m
[32m+[m[32m        android:text="stock"[m
[32m+[m[32m        app:layout_constraintBottom_toBottomOf="@+id/GoToCalendarActivity"[m
[32m+[m[32m        app:layout_constraintEnd_toEndOf="parent"[m
[32m+[m[32m        app:layout_constraintStart_toStartOf="parent"[m
[32m+[m[32m        app:layout_constraintTop_toTopOf="parent" />[m
 </androidx.constraintlayout.widget.ConstraintLayout>[m
[1mdiff --git a/app/src/main/res/layout/activity_stock.xml b/app/src/main/res/layout/activity_stock.xml[m
[1mnew file mode 100644[m
[1mindex 0000000..e190715[m
[1m--- /dev/null[m
[1m+++ b/app/src/main/res/layout/activity_stock.xml[m
[36m@@ -0,0 +1,60 @@[m
[32m+[m[32m<?xml version="1.0" encoding="utf-8"?>[m
[32m+[m[32m<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"[m
[32m+[m[32m    xmlns:app="http://schemas.android.com/apk/res-auto"[m
[32m+[m[32m    xmlns:tools="http://schemas.android.com/tools"[m
[32m+[m[32m    android:id="@+id/main"[m
[32m+[m[32m    android:layout_width="match_parent"[m
[32m+[m[32m    android:layout_height="match_parent"[m
[32m+[m[32m    tools:context=".database.stocks.StockActivity">[m
[32m+[m[32m    <TextView[m
[32m+[m[32m        android:id="@+id/stock_title"[m
[32m+[m[32m        android:layout_width="wrap_content"[m
[32m+[m[32m        android:layout_height="35dp"[m
[32m+[m[32m        android:text="@string/welcome_to_the_stock"[m
[32m+[m[32m        android:textSize="24sp"[m
[32m+[m[32m        app:layout_constraintTop_toTopOf="parent"[m
[32m+[m[32m        app:layout_constraintStart_toStartOf="parent"[m
[32m+[m[32m        app:layout_constraintEnd_toEndOf="parent" />[m
[32m+[m
[32m+[m[32m    <TextView[m
[32m+[m[32m        android:id="@+id/displayStock"[m
[32m+[m[32m        android:layout_width="412dp"[m
[32m+[m[32m        android:layout_height="550dp"[m
[32m+[m[32m        android:layout_marginStart="0dp"[m
[32m+[m[32m        android:layout_marginTop="35dp"[m
[32m+[m[32m        android:text="TextView"[m
[32m+[m[32m        app:layout_constraintStart_toStartOf="parent"[m
[32m+[m[32m        app:layout_constraintTop_toBottomOf="parent"[m
[32m+[m[32m        app:layout_constraintTop_toTopOf="parent" />[m
[32m+[m
[32m+[m[32m    <Button[m
[32m+[m[32m        android:id="@+id/addFoodInStockButton"[m
[32m+[m[32m        android:layout_width="140dp"[m
[32m+[m[32m        android:layout_height="54dp"[m
[32m+[m[32m        android:text="@string/addStock"[m
[32m+[m[32m        tools:layout_editor_absoluteX="0dp"[m
[32m+[m[32m        tools:layout_editor_absoluteY="342dp"[m
[32m+[m[32m        app:layout_constraintTop_toBottomOf="@+id/displayStock"[m
[32m+[m[32m        app:layout_constraintStart_toStartOf="parent"[m
[32m+[m[32m        />[m
[32m+[m
[32m+[m[32m    <Button[m
[32m+[m[32m        android:id="@+id/deleteFoodInStockButton"[m
[32m+[m[32m        android:layout_width="140dp"[m
[32m+[m[32m        android:layout_height="54dp"[m
[32m+[m[32m        android:text="@string/deleteStock"[m
[32m+[m[32m        app:layout_constraintStart_toEndOf="@+id/addFoodInStockButton"[m
[32m+[m[32m        app:layout_constraintTop_toBottomOf="@+id/displayStock"[m
[32m+[m[32m        />[m
[32m+[m
[32m+[m[32m    <Button[m
[32m+[m[32m        android:id="@+id/generateAListFoodInStockButton"[m
[32m+[m[32m        android:layout_width="135dp"[m
[32m+[m[32m        android:layout_height="54dp"[m
[32m+[m[32m        android:text="@string/generate_a_list"[m
[32m+[m[32m        app:layout_constraintTop_toBottomOf="@+id/displayStock"[m
[32m+[m[32m        app:layout_constraintStart_toEndOf="@id/deleteFoodInStockButton"[m
[32m+[m[32m        />[m
[32m+[m
[32m+[m
[32m+[m[32m</androidx.constraintlayout.widget.ConstraintLayout>[m
\ No newline at end of file[m
[1mdiff --git a/app/src/main/res/values/strings.xml b/app/src/main/res/values/strings.xml[m
[1mindex aba4b7f..ece007d 100644[m
[1m--- a/app/src/main/res/values/strings.xml[m
[1m+++ b/app/src/main/res/values/strings.xml[m
[36m@@ -23,5 +23,11 @@[m
     <string name="meal_name">Meal name</string>[m
     <string name="pick_date">Pick date</string>[m
     <string name="add_a_meal_to_the_calendar">Add a meal to the calendar</string>[m
[32m+[m[32m    <string name="welcome_to_the_stock">Welcome to the Stock</string>[m
[32m+[m[32m    <string name="generate_a_list">generate a list</string>[m
[32m+[m[32m    <string name="deleteStock">delete</string>[m
[32m+[m[32m    <string name="addStock">add</string>[m
[32m+[m[32m    <string name="enter_food_s_name">Enter food\'s name</string>[m
[32m+[m[32m    <string name="enter_the_amount">Enter the amount</string>[m
 [m
 </resources>[m
