package com.depauw.buckettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.depauw.buckettracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener{

    // Constants
    public static final long DEFAULT_NUM_MINS = 20;
    public static final long MILLIS_PER_MIN = 60000;
    public static final long MILLIS_PER_SEC = 1000;
    public static final long SECS_PER_MIN = 60;

    // Member Variables
    private ActivityMainBinding binding;
    private CountDownTimer countDownTimer;

    // Global Variables
    long timeLeft = DEFAULT_NUM_MINS*MILLIS_PER_MIN;

    // Creating and Returning New Object
    private CountDownTimer getNewTimer(long totalLength, long tickLength){
        return new CountDownTimer(totalLength, tickLength) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                long minute = (millisUntilFinished / MILLIS_PER_MIN) % SECS_PER_MIN;
                long second = (millisUntilFinished / MILLIS_PER_SEC) % SECS_PER_MIN;
                if(second<10 && minute>=10) binding.textviewTimeRemaining.setText(minute + ":0" + second);
                else if (minute<10 && second>=10) binding.textviewTimeRemaining.setText("0"  + minute + ":" + second);
                else if (minute<10 && second<10) binding.textviewTimeRemaining.setText("0" + minute + ":0" + second);
                else binding.textviewTimeRemaining.setText((millisUntilFinished / MILLIS_PER_MIN) % SECS_PER_MIN + ":" + (millisUntilFinished / MILLIS_PER_SEC) % SECS_PER_MIN);
            }
            @Override
            public void onFinish() {
                countDownTimer.cancel();
                 binding.textviewTimeRemaining.setText("00:00");
            }
        };
    }

    // Pressing Set Time Button
    private void setTime(){
        binding.switchGameClock.setChecked(false);
        countDownTimer.cancel();
        long newMinutes = Long.valueOf(binding.edittextNumMins.getText().toString());
        long newSeconds = Long.valueOf(binding.edittextNumSecs.getText().toString());
        long newMinutesInMilli = newMinutes * MILLIS_PER_MIN;
        long newSecondsInMilli = newSeconds * MILLIS_PER_SEC;

        if( ((newMinutes>=0 && newMinutes<20) && (newSeconds>=0 && newSeconds<60)) || (newMinutes==20 && newSeconds==0)) {
            if(newSeconds<10 && newMinutes>=10) binding.textviewTimeRemaining.setText(newMinutes + ":0" + newSeconds);
            else if (newMinutes<10 && newSeconds>=10) binding.textviewTimeRemaining.setText("0"  + newMinutes + ":" + newSeconds);
            else if (newMinutes<10 && newSeconds<10) binding.textviewTimeRemaining.setText("0" + newMinutes + ":0" + newSeconds);
            else binding.textviewTimeRemaining.setText(newMinutes + ":" + newSeconds);
            timeLeft = newMinutesInMilli + newSecondsInMilli;
            countDownTimer = getNewTimer((newMinutesInMilli + newSecondsInMilli), MILLIS_PER_SEC);
        }
    }

    // Pressing Switch Button
    private void switchState(){
        if(binding.switchGameClock.isChecked()){
            countDownTimer = getNewTimer(timeLeft, MILLIS_PER_SEC);
            countDownTimer.start();
        }
        else{
            countDownTimer.cancel();
        }
    }

    // Pressing Toggle Button
    private TextView focusToggleTeam(){
        int black  = getResources().getColor(R.color.black);
        int red = getResources().getColor(R.color.red);
        if(binding.toggleIsGuest.isChecked()){
            binding.labelGuest.setTextColor(red);
            binding.textviewGuestScore.setTextColor(red);
            binding.labelHome.setTextColor(black);
            binding.textviewHomeScore.setTextColor(black);
            return binding.textviewGuestScore;
        }
        else{
            binding.labelHome.setTextColor(red);
            binding.textviewHomeScore.setTextColor(red);
            binding.labelGuest.setTextColor(black);
            binding.textviewGuestScore.setTextColor(black);
            return binding.textviewHomeScore;
        }
    }

    // Pressing Add Score Button
    private int pointsToAdd(){
        int currentPoints = Integer.valueOf(focusToggleTeam().getText().toString());
    if(binding.checkboxAddOne.isChecked()){
        currentPoints++;
        binding.checkboxAddOne.toggle();
    }
    if(binding.checkboxAddTwo.isChecked()){
        currentPoints += 2;
        binding.checkboxAddOne.toggle();
    }
    if(binding.checkboxAddThree.isChecked()){
        currentPoints += 3;
        binding.checkboxAddOne.toggle();
    }
    return currentPoints;
    }

    // Set binding, listeners, and home score to be red
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonAddScore.setOnLongClickListener(this);
        binding.toggleIsGuest.setOnClickListener(this);
        binding.switchGameClock.setOnClickListener(this);
        binding.buttonSetTime.setOnClickListener(this);

        binding.labelHome.setTextColor(getResources().getColor(R.color.red));
        binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.red));
    }

    // Clicking Toggle, Switch, and Normal Buttons
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case (R.id.toggle_is_guest): {
                focusToggleTeam();
                break;
            }
            case (R.id.switch_game_clock): {
                switchState();
                break;
            }
            case (R.id.button_set_time):
            {
                setTime();
                break;
            }
        }
    }

    // Long Clicking Add Button
    @Override
    public boolean onLongClick(View v) {
        switch(v.getId()){
            case (R.id.button_add_score):
            {
                String pointsToAdd = String.valueOf(pointsToAdd());
                focusToggleTeam().setText(pointsToAdd);
                binding.checkboxAddOne.setChecked(false);
                binding.checkboxAddTwo.setChecked(false);
                binding.checkboxAddThree.setChecked(false);
                binding.toggleIsGuest.toggle();
                focusToggleTeam();
                return true;
            }
        }
        return true;
    }
}