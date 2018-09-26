package com.example.mcresswell.project01.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mcresswell.project01.userProfile.PhysicalStats;
import com.example.mcresswell.project01.userProfile.UserProfile;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UserProfileUtils {

    private static final String LOG = UserProfileUtils.class.getSimpleName();

    public enum BodyMassIndex {
        UNDERWEIGHT,
        HEALTHY_WEIGHT,
        OVERWEIGHT,
        OBESE,
        CLINICALLY_OBESE
    }

    public static double calculateBmi(double heightInInches, double weightInPounds) {

        final double BMI_METRIC_TO_IMPERIAL_SCALE_FACTOR = 703;
        return weightInPounds/Math.pow(heightInInches, 2) * BMI_METRIC_TO_IMPERIAL_SCALE_FACTOR;
    }

    public static BodyMassIndex getBmi(double bmiValue) {
        final double UNDERWEIGHT_THRESHOLD = 18.5;
        final double AVERAGE_THRESHOLD = 25.0;
        final double OVERWEIGHT_THRESHOLD = 30.0;
        final double OBESE_THRESHOLD = 35.0;

        return bmiValue < UNDERWEIGHT_THRESHOLD ? BodyMassIndex.UNDERWEIGHT :
                bmiValue < AVERAGE_THRESHOLD ? BodyMassIndex.HEALTHY_WEIGHT :
                        bmiValue < OVERWEIGHT_THRESHOLD ? BodyMassIndex.OVERWEIGHT :
                                bmiValue < OBESE_THRESHOLD ? BodyMassIndex.OBESE :
                                        BodyMassIndex.CLINICALLY_OBESE;
    }

    public static double calculateBMR(PhysicalStats stats) {
        double BMR = 0.0;
        double totalHeightInInches = (stats.getHeightFeet() * 12.0) + stats.getHeightInches();

        //calculate BMR based on sex of user
        if(stats.getSex().equalsIgnoreCase("F")||
                stats.getSex().equalsIgnoreCase("Female")) {
            //user is female, s = -161
            BMR = (9.99 * stats.getWeightInPounds()) +
                    (6.25 * totalHeightInInches) - 4.92 * stats.getAge() - 161;
        }
        else if (stats.getSex().equalsIgnoreCase("M") ||
                stats.getSex().equalsIgnoreCase("Male")) {
            //user is male, s = 5
            BMR = (9.99 * stats.getWeightInPounds()) +
                    (6.25 * totalHeightInInches) - 4.92 * stats.getAge() + 5;
        } else {
            Log.d(LOG, "Cannot calculate BMR; invalid gender.");
            //--TODO: throw an error here if not male or female--//
        }

        return BMR;
    }

    //http://www.bmrcalculator.org
    //You exercise moderately (3-5 days per week)	Calories Burned a Day = BMR x 1.55
    //Low	You get little to no exercise	Calories Burned a Day = BMR x 1.2
    //In order to lose 1 pound of fat each week, you must have a deficit of 3,500 calories over the course of a week.[5]
    // https://www.wikihow.com/Calculate-How-Many-Calories-You-Need-to-Eat-to-Lose-Weight
    public static double calculateCalories(UserProfile profile) {
        double numOfCalories;

//        double BMR = calculateBMR(profile.getBodyData());
        double BMR = 1500;

        if (profile.getM_lifestyleSelection().equalsIgnoreCase("Sedentary")) {
            numOfCalories = BMR * 1.2;

            if(profile.getM_weightGoal().equalsIgnoreCase("Gain")) {
                numOfCalories += profile.getM_lbsPerWeek() + 3500;
            } else if(profile.getM_weightGoal().equalsIgnoreCase("Lose")){
                numOfCalories += profile.getM_lbsPerWeek() - 3500;
            }

        }
        else {
            //active
            numOfCalories = BMR * 1.55;

            if(profile.getM_weightGoal().equalsIgnoreCase("Gain")) {
                numOfCalories += BMR + profile.getM_lbsPerWeek() + 3500;
            } else if(profile.getM_weightGoal().equalsIgnoreCase("Loose")){
                numOfCalories += BMR + profile.getM_lbsPerWeek() - 3500;
            }
        }

        return numOfCalories;
    }

    public static double calculateHeightInInches(double feet, double inches) {
        return feet * 12.0 + inches;
    }


    //helper functions
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int calculateAge(String m_dob) {
        DateTimeFormatter dob_format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        dob_format = dob_format.withLocale(Locale.US);

        LocalDate dob = LocalDate.parse(m_dob, dob_format);
        LocalDate today = LocalDate.now();

        return Period.between(dob, today).getYears();
    }
}
