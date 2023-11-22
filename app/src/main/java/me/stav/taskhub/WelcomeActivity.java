package me.stav.taskhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.stav.taskhub.utilities.MyViewPagerAdapter;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private TextView firstTitle, firstDescription, secondTitle, secondDescription;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing firebase auth
        fbAuth = FirebaseAuth.getInstance();

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Checking if user is already logged in
        if (fbAuth.getCurrentUser() != null) {
            launchHomeScreen();
            finish();
        }

        setContentView(R.layout.activity_welcome);

        initializeValues();

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();
    }

    // Function gets the current page, and sets the dots' color
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        // Getting the color array
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        // Clearing current dots from the layout
        dotsLayout.removeAllViews();

        // Setting the new dots with the unactive color
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml(";"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        // Changing the current page colors
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT' and adding the texts from the files
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);

                secondTitle = findViewById(R.id.second_title);
                secondDescription = findViewById(R.id.second_description);

                setTextsForSlidersContent(R.raw.second_title, secondTitle);
                setTextsForSlidersContent(R.raw.second_description, secondDescription);
            } else {
                // still pages are left and adding the texts from the files
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);

                firstTitle = findViewById(R.id.first_title);
                firstDescription = findViewById(R.id.first_description);

                setTextsForSlidersContent(R.raw.first_title, firstTitle);
                setTextsForSlidersContent(R.raw.first_description, firstDescription);
            }
        }

        // Adding the texts for the first view
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            firstTitle = findViewById(R.id.first_title);
            firstDescription = findViewById(R.id.first_description);

            setTextsForSlidersContent(R.raw.first_title, firstTitle);
            setTextsForSlidersContent(R.raw.first_description, firstDescription);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    // Making notification bar transparent
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    // Initializing all values
    private void initializeValues() {
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);

        // layouts of welcome sliders
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2
        };

        // Setting the new page adapter
        myViewPagerAdapter = new MyViewPagerAdapter(WelcomeActivity.this, this.layouts);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        // Checking the skip btn click
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRegisterScreen();
            }
        });

        // Checking the next btn, move to home screen or next swipe screen
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page if true launch RegisterActivity
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchRegisterScreen();
                }
            }
        });

    }

    // Starting register screen
    private void launchRegisterScreen() {
        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    // Reading the text from a file, and setting it into a textview
    private void setTextsForSlidersContent(int rawResourceId, TextView textView) {
        InputStream inputStream = getResources().openRawResource(rawResourceId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str, fullText = "";

        while (true) {
            try {
                str = bufferedReader.readLine();
                if (str == null) break;
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }

            fullText += str + "\n";
        }

        try {
            bufferedReader.close();
            textView.setText(fullText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}