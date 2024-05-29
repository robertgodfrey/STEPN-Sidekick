package stepn.sidekick.stepnsidekick;

import static android.content.Context.MODE_PRIVATE;
import static stepn.sidekick.stepnsidekick.Finals.COMF;
import static stepn.sidekick.stepnsidekick.Finals.EFF;
import static stepn.sidekick.stepnsidekick.Finals.GMT_NUM_A;
import static stepn.sidekick.stepnsidekick.Finals.GMT_NUM_B;
import static stepn.sidekick.stepnsidekick.Finals.GMT_NUM_C;
import static stepn.sidekick.stepnsidekick.Finals.LUCK;
import static stepn.sidekick.stepnsidekick.Finals.RES;
import static stepn.sidekick.stepnsidekick.Finals.PREFERENCES_ID;
import static stepn.sidekick.stepnsidekick.Finals.COLOR_ALMOST_BLACK;
import static stepn.sidekick.stepnsidekick.Finals.COLOR_WHITE;
import static stepn.sidekick.stepnsidekick.Finals.COLOR_GANDALF;
import static stepn.sidekick.stepnsidekick.Finals.COLOR_RED;
import static stepn.sidekick.stepnsidekick.Finals.COLOR_GEM_SOCKET_SHADOW;
import static stepn.sidekick.stepnsidekick.Finals.COLOR_PROGRESS_GMT;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Shoe optimizer fragment. Uses community data to determine best points allocation for GST earning
 * and mystery box chance.
 *
 * @author Rob Godfrey
 * @version 1.7.3 switch API from vercel
 *
 */

public class OptimizerFrag extends Fragment {

    // holy preferences batman
    private final String OPT_SHOE_TYPE_PREF = "optShoe";
    private final String OPT_ENERGY_PREF = "optEnergy";
    private final String SHOE_NAME = "shoeName";
    private final String SHOE_IMAGE_URL = "shoeImageUrl";
    private final String SHOE_RARITY_PREF = "shoeRarity";
    private final String SHOE_LEVEL_PREF = "shoeLevel";
    private final String BASE_EFF_PREF = "baseEff";
    private final String ADDED_EFF_PREF = "addedEff";
    private final String BASE_LUCK_PREF = "baseLuck";
    private final String ADDED_LUCK_PREF = "addedLuck";
    private final String BASE_COMF_PREF = "baseComf";
    private final String ADDED_COMF_PREF = "addedComf";
    private final String BASE_RES_PREF = "baseRes";
    private final String ADDED_RES_PREF = "addedRes";
    private final String GEM_ONE_TYPE_PREF = "gemOneType";
    private final String GEM_ONE_RARITY_PREF = "gemOneRarity";
    private final String GEM_ONE_MOUNTED_PREF = "gemOneGem";
    private final String GEM_TWO_TYPE_PREF = "gemTwoType";
    private final String GEM_TWO_RARITY_PREF = "gemTwoRarity";
    private final String GEM_TWO_MOUNTED_PREF = "gemTwoGem";
    private final String GEM_THREE_TYPE_PREF = "gemThreeType";
    private final String GEM_THREE_RARITY_PREF = "gemThreeRarity";
    private final String GEM_THREE_MOUNTED_PREF = "gemThreeGem";
    private final String GEM_FOUR_TYPE_PREF = "gemFourType";
    private final String GEM_FOUR_RARITY_PREF = "gemFourRarity";
    private final String GEM_FOUR_MOUNTED_PREF = "gemFourGem";
    private final String COMF_GEM_HP_REPAIR = "hpRepairGem";
    private final String UPDATE_PREF = "optimizerUpdate";
    private final String ONE_TWENTY_FIVE_BOOL_PREF = "oneTwentyFiveBool";
    private final String ONE_TWENTY_FIVE_ENERGY_PREF = "oneTwentyFiveEnergy";
    private final String SHOE_NUM_PREF = "shoeNum";
    private final String GMT_EARNING_PREF = "gmtEarning";
    private final String CHAIN_PREF = "chain";
    private final String GMT_PERCENT_MODIFIER_PREF = "gmtPercentModifier";
    private final String SHOE_LOCKED_PREF = "shoeLocked";

    private final int SOL = 1;
    private final int BSC = 3;
    private final int ETH = 5;

    private final int COMMON = 2;
    private final int UNCOMMON = 3;
    private final int RARE = 4;
    private final int EPIC = 5;
    private final int WALKER = 0;
    private final int JOGGER = 1;
    private final int RUNNER = 2;
    private final int TRAINER = 3;

    ImageButton shoeRarityButton, shoeTypeButton, optimizeGstGmtButton, optimizeLuckButton, backgroundButton,
            chainSelectButton, coinSelectButton;
    Button gemSocketOneButton, gemSocketTwoButton, gemSocketThreeButton, gemSocketFourButton,
            subEffButton, addEffButton, subLuckButton, addLuckButton, subComfButton, addComfButton,
            subResButton, addResButton, changeComfGemButton, leftButton, rightButton, resetButton,
            oneTwentyFiveButton, changeGmtEstimateButton, changeGstLimitButton, toggleGstEstLimitButton,
            shoeLockedButton;
    SeekBar levelSeekbar;
    EditText energyEditText, effEditText, luckEditText, comfortEditText, resEditText, focusThief,
            shoeNameEditText, comfGemPriceEditText;

    TextView shoeRarityTextView, shoeTypeTextView, levelTextView, effTotalTextView, luckTotalTextView,
            comfortTotalTextView, resTotalTextView, pointsAvailableTextView, estGstGmtTextView,
            gstLimitTextView, durabilityLossTextView, repairCostDurTextView, totalIncomeTextView,
            effMinusTv, effPlusTv, luckMinusTv, luckPlusTv, comfMinusTv, comfPlusTv, resMinusTv,
            resPlusTv, optimizeGstGmtTextView, shoeRarityShadowTextView, shoeTypeShadowTextView,
            hpLossTextView, repairCostHpTextView, gemMultipleTextView, gemMultipleTotalTextView,
            optimizeLuckTextView, shoeOneTextView, shoeTwoTextView, shoeThreeTextView,
            totalIncomeUsdTextView, oneTwentyFiveTextView, estGstGmtLabelTv, gstLimitPerDaySlashTextView,
            gmtTotalMinusTv, gmtTotalTv, optimizeGstGmtTextViewShadow, chainSelectTv, chainSelectShadowTv,
            coinSelectTv, coinShadowSelectTv, mb1Percent, mb2Percent, mb3Percent, mb4Percent, mb5Percent,
            mb6Percent, mb7Percent, mb8Percent, mb9Percent, mb10Percent;

    ImageView gemSocketOne, gemSocketOneShadow, gemSocketOneLockPlus, gemSocketTwo,
            gemSocketTwoShadow, gemSocketTwoLockPlus, gemSocketThree, gemSocketThreeShadow,
            gemSocketThreeLockPlus, gemSocketFour, gemSocketFourShadow, gemSocketFourLockPlus,
            shoeTypeImageView, shoeCircles, shoeRarityButtonShadow, shoeTypeButtonShadow, minLevelImageView,
            optimizeGstGmtButtonShadow, mysteryBox1, mysteryBox2, mysteryBox3, mysteryBox4, mysteryBox5,
            mysteryBox6, mysteryBox7, mysteryBox8, mysteryBox9, mysteryBox10, footOne, footTwo, footThree, energyBox,
            comfGemHpRepairImageView, comfGemHpRepairTotalImageView, optimizeLuckButtonShadow,
            shoeNameBoxImageView, footOneShadow, footTwoShadow, footThreeShadow, estGstGmtIcon,
            totalGmtIcon, chainSelectShadow, coinSelectShadow, coinSelectIcon, coinSelectIconShadow,
            gemPriceBox, shoeLockedImageView;


    LinearLayout shoeTypeLayout, shoeTypeLayoutShadow;
    ProgressBar mbLoadingSpinner;

    private int shoeRarity, shoeType, shoeLevel, pointsAvailable, gstLimit, addedEff, addedLuck,
            addedComf, addedRes, comfGemLvlForRepair, gstCostBasedOnGem, shoeNum, shoeChain,
            gmtPercentModifier;
    private float baseMin, baseMax, baseEff, baseLuck, baseComf, baseRes, gemEff, gemLuck, gemComf,
            gemRes, dpScale, energy, hpPercentRestored, comfGemMultiplier, oneTwentyFiveEnergy,
            prevMbEnergy, prevMbLuck;
    private boolean saveNewGem, update, oneTwentyFive, gmtEarningOn, useGstLimit, fragActive,
            shoeLocked;
    private double hpLoss, comfGemPrice, gmtNumA, gmtNumB, gmtNumC;
    private String shoeName, shoeImageUrl;

    private double[] TOKEN_PRICES;
    private double[] GEM_PRICES;
    private int[] mbLuckIndices;
    private int[][] mbProbabilities;

    ArrayList<Gem> gems;

    public OptimizerFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragActive = true;

        // inits gem array first so no null pointer if loading from disk slow
        gems = new ArrayList<>();
        gems.add(new Gem(-1,0,0));
        gems.add(new Gem(-1,0,0));
        gems.add(new Gem(-1,0,0));
        gems.add(new Gem(-1,0,0));

        // init gmt nums
        gmtNumA = 0.0696;
        gmtNumB = 0.4821;
        gmtNumC = 0.25;

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getSharedPrefs = requireActivity().getSharedPreferences(PREFERENCES_ID, MODE_PRIVATE);
                shoeNum = getSharedPrefs.getInt(SHOE_NUM_PREF, 0);

                final String shoeNumString = (shoeNum == 0 ? "" : String.valueOf(shoeNum));

                energy = getSharedPrefs.getFloat(OPT_ENERGY_PREF + shoeNumString, 0);
                oneTwentyFiveEnergy = getSharedPrefs.getFloat(ONE_TWENTY_FIVE_ENERGY_PREF + shoeNumString, 0);
                shoeType = getSharedPrefs.getInt(OPT_SHOE_TYPE_PREF + shoeNumString, WALKER);
                shoeRarity = getSharedPrefs.getInt(SHOE_RARITY_PREF + shoeNumString, COMMON);
                shoeLevel = getSharedPrefs.getInt(SHOE_LEVEL_PREF + shoeNumString, 1);
                baseEff = getSharedPrefs.getFloat(BASE_EFF_PREF + shoeNumString, 0);
                addedEff = getSharedPrefs.getInt(ADDED_EFF_PREF + shoeNumString, 0);
                baseLuck = getSharedPrefs.getFloat(BASE_LUCK_PREF + shoeNumString, 0);
                addedLuck = getSharedPrefs.getInt(ADDED_LUCK_PREF + shoeNumString, 0);
                baseComf = getSharedPrefs.getFloat(BASE_COMF_PREF + shoeNumString, 0);
                addedComf = getSharedPrefs.getInt(ADDED_COMF_PREF + shoeNumString, 0);
                baseRes = getSharedPrefs.getFloat(BASE_RES_PREF + shoeNumString, 0);
                addedRes = getSharedPrefs.getInt(ADDED_RES_PREF + shoeNumString, 0);
                comfGemLvlForRepair = getSharedPrefs.getInt(COMF_GEM_HP_REPAIR, 1);
                update = getSharedPrefs.getBoolean(UPDATE_PREF, true);
                shoeName = getSharedPrefs.getString(SHOE_NAME + shoeNumString, "");
                shoeImageUrl = getSharedPrefs.getString(SHOE_IMAGE_URL + shoeNumString, "");
                oneTwentyFive = getSharedPrefs.getBoolean(ONE_TWENTY_FIVE_BOOL_PREF + shoeNumString, false);
                gmtEarningOn = getSharedPrefs.getBoolean(GMT_EARNING_PREF + shoeNumString, false);
                shoeChain = getSharedPrefs.getInt(CHAIN_PREF + shoeNumString, SOL);
                gmtPercentModifier = getSharedPrefs.getInt(GMT_PERCENT_MODIFIER_PREF, 100);
                shoeLocked = getSharedPrefs.getBoolean(SHOE_LOCKED_PREF + shoeNumString, false);

                gmtNumA = getSharedPrefs.getFloat(GMT_NUM_A, 0.0538f);
                gmtNumB = getSharedPrefs.getFloat(GMT_NUM_B, 0.4741f);
                gmtNumC = getSharedPrefs.getFloat(GMT_NUM_C, 0);

                dpScale = getResources().getDisplayMetrics().density;

                gems.get(0).setSocketType(getSharedPrefs.getInt(GEM_ONE_TYPE_PREF + shoeNumString, -1));
                gems.get(0).setSocketRarity(getSharedPrefs.getInt(GEM_ONE_RARITY_PREF + shoeNumString, 0));
                gems.get(0).setMountedGem(getSharedPrefs.getInt(GEM_ONE_MOUNTED_PREF + shoeNumString, 0));

                gems.get(1).setSocketType(getSharedPrefs.getInt(GEM_TWO_TYPE_PREF + shoeNumString, -1));
                gems.get(1).setSocketRarity(getSharedPrefs.getInt(GEM_TWO_RARITY_PREF + shoeNumString, 0));
                gems.get(1).setMountedGem(getSharedPrefs.getInt(GEM_TWO_MOUNTED_PREF + shoeNumString, 0));

                gems.get(2).setSocketType(getSharedPrefs.getInt(GEM_THREE_TYPE_PREF + shoeNumString, -1));
                gems.get(2).setSocketRarity(getSharedPrefs.getInt(GEM_THREE_RARITY_PREF + shoeNumString, 0));
                gems.get(2).setMountedGem(getSharedPrefs.getInt(GEM_THREE_MOUNTED_PREF + shoeNumString, 0));

                gems.get(3).setSocketType(getSharedPrefs.getInt(GEM_FOUR_TYPE_PREF + shoeNumString, -1));
                gems.get(3).setSocketRarity(getSharedPrefs.getInt(GEM_FOUR_RARITY_PREF + shoeNumString, 0));
                gems.get(3).setMountedGem(getSharedPrefs.getInt(GEM_FOUR_MOUNTED_PREF + shoeNumString, 0));
            }
        }).start();

        Thread fetchTokenPrices = new Thread(new Runnable() {
            @Override
            public void run() {
                final String GECKO_BASE_URL = "https://api.coingecko.com/";

                // from 0 - 6: GMT, SOL, GST-SOL, BNB, GST-BNB, ETH, GST-ETH
                TOKEN_PRICES = new double[]{0, 0, 0, 0, 0, 0, 0};

                Retrofit retrofit = new Retrofit.Builder().baseUrl(GECKO_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                TokenApi myTokenApi = retrofit.create(TokenApi.class);

                Call<TokenPrices> call = myTokenApi.getPosts();

                call.enqueue(new Callback<TokenPrices>() {
                    @Override
                    public void onResponse(Call<TokenPrices> call, Response<TokenPrices> response) {

                        try {
                            TokenPrices priceList = response.body();
                            TOKEN_PRICES[0] = priceList.getGmtPrice();
                            TOKEN_PRICES[1] = priceList.getSolanaPrice();
                            TOKEN_PRICES[2] = priceList.getGstSol();
                            TOKEN_PRICES[3] = priceList.getBinancecoin();
                            TOKEN_PRICES[4] = priceList.getGstBsc();
                            TOKEN_PRICES[5] = priceList.getEthereum();
                            TOKEN_PRICES[6] = priceList.getGstEth();

                            if (GEM_PRICES[0] != 0 && fragActive) {
                                calcTotals();
                            }

                        } catch (NullPointerException e) {
                            if (fragActive) {
                                Toast.makeText(requireActivity(), "Unable to get token prices. Try again in a few moments", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenPrices> call, Throwable t) {
                        if (fragActive) {
                            Toast.makeText(requireActivity(), "Unable to get token prices. Try again in a few moments", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                GEM_PRICES = new double[]{0, 0, 0};
                getGemPrices();
            }
        });
        fetchTokenPrices.start();
        try {
            fetchTokenPrices.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_optimizer, container, false);

        shoeRarityButton = view.findViewById(R.id.shoeRarityButton);
        shoeTypeButton = view.findViewById(R.id.shoeTypeButton);
        optimizeGstGmtButton = view.findViewById(R.id.optimizeGmtButton);
        optimizeLuckButton = view.findViewById(R.id.optimizeLuckButton);
        leftButton = view.findViewById(R.id.leftArrowButton);
        rightButton = view.findViewById(R.id.rightArrowButton);
        resetButton = view.findViewById(R.id.resetPageButton);
        oneTwentyFiveButton = view.findViewById(R.id.oneTwentyFiveButton);

        gemSocketOneButton = view.findViewById(R.id.gemSocketOneButton);
        gemSocketTwoButton = view.findViewById(R.id.gemSocketTwoButton);
        gemSocketThreeButton = view.findViewById(R.id.gemSocketThreeButton);
        gemSocketFourButton = view.findViewById(R.id.gemSocketFourButton);

        shoeNameBoxImageView = view.findViewById(R.id.shoeNameImageView);
        shoeLockedImageView = view.findViewById(R.id.shoeLock);
        shoeLockedButton = view.findViewById(R.id.shoeLockButton);
        comfGemHpRepairImageView = view.findViewById(R.id.comfGemHpRepair);
        changeComfGemButton = view.findViewById(R.id.changeComfGemButton);
        changeGmtEstimateButton = view.findViewById(R.id.changeGmtEstimateButton);
        changeGstLimitButton = view.findViewById(R.id.changeGstLimitButton);
        toggleGstEstLimitButton = view.findViewById(R.id.toggleGstEstLimitButton);
        gemMultipleTextView = view.findViewById(R.id.gemMultipleTextView);
        gemMultipleTotalTextView = view.findViewById(R.id.gemMultipleTotalTextView);
        comfGemHpRepairTotalImageView = view.findViewById(R.id.comfGemHpTotalRepair);

        subEffButton = view.findViewById(R.id.subEffButton);
        addEffButton = view.findViewById(R.id.addEffButton);
        subLuckButton = view.findViewById(R.id.subLuckButton);
        addLuckButton = view.findViewById(R.id.addLuckButton);
        subComfButton = view.findViewById(R.id.subComfButton);
        addComfButton = view.findViewById(R.id.addComfButton);
        subResButton = view.findViewById(R.id.subResButton);
        addResButton = view.findViewById(R.id.addResButton);

        estGstGmtLabelTv = view.findViewById(R.id.estGstGmtLabelTextView);
        gstLimitPerDaySlashTextView = view.findViewById(R.id.gstLimitPerDaySlashTextView);
        estGstGmtIcon = view.findViewById(R.id.gstGmtIconLimit);
        gmtTotalMinusTv = view.findViewById(R.id.gmtTotalMinusTextView);
        gmtTotalTv = view.findViewById(R.id.gmtIncomeTextView);
        totalGmtIcon = view.findViewById(R.id.gmtIconTotal);

        backgroundButton = view.findViewById(R.id.backgroundThingButton);
        levelSeekbar = view.findViewById(R.id.levelSeekBar);

        shoeNameEditText = view.findViewById(R.id.shoeNameEditText);
        energyEditText = view.findViewById(R.id.energyToSpendOptimizerEditText);
        effEditText = view.findViewById(R.id.baseEffEditText);
        luckEditText = view.findViewById(R.id.baseLuckEditText);
        comfortEditText = view.findViewById(R.id.baseComfortEditText);
        resEditText = view.findViewById(R.id.baseResEditText);
        focusThief = view.findViewById(R.id.focusThief);

        shoeOneTextView = view.findViewById(R.id.shoeOneTextView);
        shoeTwoTextView = view.findViewById(R.id.shoeTwoTextView);
        shoeThreeTextView = view.findViewById(R.id.shoeThreeTextView);
        shoeRarityTextView = view.findViewById(R.id.shoeRarityTextView);
        shoeRarityShadowTextView = view.findViewById(R.id.shoeRarityShadowTextView);
        shoeTypeTextView = view.findViewById(R.id.shoeTypeTextView);
        shoeTypeShadowTextView = view.findViewById(R.id.shoeTypeShadowTextView);
        levelTextView = view.findViewById(R.id.levelTextView);
        effTotalTextView = view.findViewById(R.id.totalEffTextView);
        luckTotalTextView = view.findViewById(R.id.totalLuckTextView);
        comfortTotalTextView = view.findViewById(R.id.totalComfTextView);
        resTotalTextView = view.findViewById(R.id.totalResTextView);
        pointsAvailableTextView = view.findViewById(R.id.pointsTextView);
        estGstGmtTextView = view.findViewById(R.id.gstPerDayTextView);
        gstLimitTextView = view.findViewById(R.id.gstLimitPerDayTextView);
        durabilityLossTextView = view.findViewById(R.id.durabilityLossTextView);
        repairCostDurTextView = view.findViewById(R.id.repairCostDurTextView);
        totalIncomeTextView = view.findViewById(R.id.gstIncomeTextView);
        optimizeGstGmtTextView = view.findViewById(R.id.optimizeGstGmtTextView);
        optimizeGstGmtTextViewShadow = view.findViewById(R.id.optimizeGstGmtTextViewShadow);
        optimizeLuckTextView = view.findViewById(R.id.optimizeLuckTextView);
        hpLossTextView = view.findViewById(R.id.hpLossTextView);
        repairCostHpTextView = view.findViewById(R.id.repairCostHpTextView);
        oneTwentyFiveTextView = view.findViewById(R.id.oneTwentyFiveTextView);

        effMinusTv = view.findViewById(R.id.subEffTextView);
        effPlusTv = view.findViewById(R.id.addEffTextView);
        luckMinusTv = view.findViewById(R.id.subLuckTextView);
        luckPlusTv = view.findViewById(R.id.addLuckTextView);
        comfMinusTv = view.findViewById(R.id.subComfTextView);
        comfPlusTv = view.findViewById(R.id.addComfTextView);
        resMinusTv = view.findViewById(R.id.subResTextView);
        resPlusTv = view.findViewById(R.id.addResTextView);

        gemSocketOne = view.findViewById(R.id.gemSocketOne);
        gemSocketOneShadow = view.findViewById(R.id.gemSocketOneShadow);
        gemSocketOneLockPlus = view.findViewById(R.id.gemSocketOneLockPlus);
        gemSocketTwo = view.findViewById(R.id.gemSocketTwo);
        gemSocketTwoShadow = view.findViewById(R.id.gemSocketTwoShadow);
        gemSocketTwoLockPlus = view.findViewById(R.id.gemSocketTwoLockPlus);
        gemSocketThree = view.findViewById(R.id.gemSocketThree);
        gemSocketThreeShadow = view.findViewById(R.id.gemSocketThreeShadow);
        gemSocketThreeLockPlus = view.findViewById(R.id.gemSocketThreeLockPlus);
        gemSocketFour = view.findViewById(R.id.gemSocketFour);
        gemSocketFourShadow = view.findViewById(R.id.gemSocketFourShadow);
        gemSocketFourLockPlus = view.findViewById(R.id.gemSocketFourLockPlus);

        shoeTypeImageView = view.findViewById(R.id.shoeTypeImageView);
        shoeCircles = view.findViewById(R.id.shoeBackground);
        shoeRarityButtonShadow = view.findViewById(R.id.shoeRarityBoxShadow);
        shoeTypeButtonShadow = view.findViewById(R.id.shoeTypeBoxShadow);
        energyBox = view.findViewById(R.id.energyBoxOptimizer);
        minLevelImageView = view.findViewById(R.id.seekbarMinLevel);
        optimizeGstGmtButtonShadow = view.findViewById(R.id.optimizeGstButtonShadow);
        optimizeLuckButtonShadow = view.findViewById(R.id.optimizeLuckButtonShadow);

        chainSelectButton = view.findViewById(R.id.chainButton);
        chainSelectTv = view.findViewById(R.id.chainTextView);
        chainSelectShadow = view.findViewById(R.id.chainBoxShadow);
        chainSelectShadowTv = view.findViewById(R.id.chainShadowTextView);
        coinSelectButton = view.findViewById(R.id.coinTypeButton);
        coinSelectTv = view.findViewById(R.id.coinTextView);
        coinSelectIcon = view.findViewById(R.id.coinImageView);
        coinSelectShadow = view.findViewById(R.id.coinBoxShadow);
        coinShadowSelectTv = view.findViewById(R.id.coinTextViewShadow);
        coinSelectIconShadow = view.findViewById(R.id.coinImageViewShadow);
        comfGemPriceEditText = view.findViewById(R.id.gemPriceEditText);
        gemPriceBox = view.findViewById(R.id.gemPriceBox);
        totalIncomeUsdTextView = view.findViewById(R.id.totalIncomeUsdTextView);

        mysteryBox1 = view.findViewById(R.id.mysteryBoxLvl1);
        mysteryBox2 = view.findViewById(R.id.mysteryBoxLvl2);
        mysteryBox3 = view.findViewById(R.id.mysteryBoxLvl3);
        mysteryBox4 = view.findViewById(R.id.mysteryBoxLvl4);
        mysteryBox5 = view.findViewById(R.id.mysteryBoxLvl5);
        mysteryBox6 = view.findViewById(R.id.mysteryBoxLvl6);
        mysteryBox7 = view.findViewById(R.id.mysteryBoxLvl7);
        mysteryBox8 = view.findViewById(R.id.mysteryBoxLvl8);
        mysteryBox9 = view.findViewById(R.id.mysteryBoxLvl9);
        mysteryBox10 = view.findViewById(R.id.mysteryBoxLvl10);

        mb1Percent = view.findViewById(R.id.mbLvl1Percent);
        mb2Percent = view.findViewById(R.id.mbLvl2Percent);
        mb3Percent = view.findViewById(R.id.mbLvl3Percent);
        mb4Percent = view.findViewById(R.id.mbLvl4Percent);
        mb5Percent = view.findViewById(R.id.mbLvl5Percent);
        mb6Percent = view.findViewById(R.id.mbLvl6Percent);
        mb7Percent = view.findViewById(R.id.mbLvl7Percent);
        mb8Percent = view.findViewById(R.id.mbLvl8Percent);
        mb9Percent = view.findViewById(R.id.mbLvl9Percent);
        mb10Percent = view.findViewById(R.id.mbLvl10Percent);

        mbLoadingSpinner = (ProgressBar)view.findViewById(R.id.mbLoadingSpinner);

        footOne = view.findViewById(R.id.footprint1ImageView);
        footTwo = view.findViewById(R.id.footprint2ImageView);
        footThree = view.findViewById(R.id.footprint3ImageView);
        footOneShadow = view.findViewById(R.id.footprint1ShadowImageView);
        footTwoShadow = view.findViewById(R.id.footprint2ShadowImageView);
        footThreeShadow = view.findViewById(R.id.footprint3ShadowImageView);

        shoeTypeLayout = view.findViewById(R.id.shoeTypeLayout);
        shoeTypeLayoutShadow = view.findViewById(R.id.shoeTypeLayoutShadow);

        mbLoadingSpinner.setVisibility(View.GONE);

        backgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFocus(view);
            }
        });

        gemSocketOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLevel < 5) {
                    Toast.makeText(getContext(), "Socket available at level 5", Toast.LENGTH_SHORT).show();
                } else {
                    chooseSocketType(0);
                }
                clearFocus(view);
            }
        });

        gemSocketTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLevel < 10) {
                    Toast.makeText(getContext(), "Socket available at level 10", Toast.LENGTH_SHORT).show();
                } else {
                    chooseSocketType(1);
                }
                clearFocus(view);
            }
        });

        gemSocketThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLevel < 15) {
                    Toast.makeText(getContext(), "Socket available at level 15", Toast.LENGTH_SHORT).show();
                } else {
                    chooseSocketType(2);
                }
                clearFocus(view);
            }
        });

        gemSocketFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLevel < 20) {
                    Toast.makeText(getContext(), "Socket available at level 20", Toast.LENGTH_SHORT).show();
                } else {
                    chooseSocketType(3);
                }
                clearFocus(view);
            }
        });

        shoeLockedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoeLocked = !shoeLocked;
                updateShoeLockStatus();
                Toast.makeText(getContext(), "Shoe attributes " + (shoeLocked ? "locked" : "unlocked"), Toast.LENGTH_SHORT).show();
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int oldShoeNum = shoeNum;

                if (shoeNum == 0) {
                    shoeNum = 5;
                } else {
                    shoeNum--;
                }

                shoeTypeImageView.setScaleX(1.1f);
                shoeTypeImageView.setScaleY(1.1f);
                ObjectAnimator scaler = ObjectAnimator.ofPropertyValuesHolder(
                        shoeTypeImageView,
                        PropertyValuesHolder.ofFloat("scaleX", 1f),
                        PropertyValuesHolder.ofFloat("scaleY", 1f));
                scaler.setDuration(1000);
                scaler.start();

                clearFocus(view);
                updatePageNewShoe(oldShoeNum);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int oldShoeNum = shoeNum;

                if (shoeNum == 5) {
                    shoeNum = 0;
                } else {
                    shoeNum++;
                }

                shoeTypeImageView.setScaleX(1.1f);
                shoeTypeImageView.setScaleY(1.1f);
                ObjectAnimator scaler = ObjectAnimator.ofPropertyValuesHolder(
                        shoeTypeImageView,
                        PropertyValuesHolder.ofFloat("scaleX", 1f),
                        PropertyValuesHolder.ofFloat("scaleY", 1f));
                scaler.setDuration(1000);
                scaler.start();

                clearFocus(view);
                updatePageNewShoe(oldShoeNum);
            }
        });

        shoeCircles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                changeShoeImage();
            }
        });

        shoeRarityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (shoeRarity == 5) {
                    shoeRarity = 2;
                } else {
                    shoeRarity++;
                }
                updateRarity();

                shoeCircles.setScaleX(1.1f);
                shoeCircles.setScaleY(1.1f);
                ObjectAnimator scaler = ObjectAnimator.ofPropertyValuesHolder(
                        shoeCircles,
                        PropertyValuesHolder.ofFloat("scaleX", 1f),
                        PropertyValuesHolder.ofFloat("scaleY", 1f));
                scaler.setDuration(1000);
                scaler.start();

                clearFocus(view);
            }
        });

        shoeRarityButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (shoeLocked) {
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        shoeRarityButton.setVisibility(View.INVISIBLE);
                        shoeRarityTextView.setVisibility(View.INVISIBLE);
                        shoeRarityShadowTextView.setText(shoeRarityTextView.getText().toString());
                        switch (shoeRarity) {
                            case UNCOMMON:
                                shoeRarityButtonShadow.setImageResource(R.drawable.button_uncommon);
                                break;
                            case RARE:
                                shoeRarityButtonShadow.setImageResource(R.drawable.button_rare);
                                break;
                            case EPIC:
                                shoeRarityButtonShadow.setImageResource(R.drawable.button_epic);
                                break;
                            default:
                                shoeRarityButtonShadow.setImageResource(R.drawable.button_common);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        shoeRarityButton.setVisibility(View.VISIBLE);
                        shoeRarityTextView.setVisibility(View.VISIBLE);
                        shoeRarityButtonShadow.setImageResource(R.drawable.button_main_shadow);
                        break;
                }
                return false;
            }
        });

        shoeTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (shoeType == 3) {
                    shoeType = 0;
                } else {
                    shoeType++;
                }
                updateType();
                calcTotals();

                shoeTypeImageView.setScaleX(1.1f);
                shoeTypeImageView.setScaleY(1.1f);
                ObjectAnimator scaler = ObjectAnimator.ofPropertyValuesHolder(
                        shoeTypeImageView,
                        PropertyValuesHolder.ofFloat("scaleX", 1f),
                        PropertyValuesHolder.ofFloat("scaleY", 1f));
                scaler.setDuration(1000);
                scaler.start();

                clearFocus(view);
            }
        });

        shoeTypeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (shoeLocked) {
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        shoeTypeButton.setVisibility(View.INVISIBLE);
                        shoeTypeLayout.setVisibility(View.INVISIBLE);
                        shoeTypeShadowTextView.setText(shoeTypeTextView.getText().toString());
                        switch (shoeRarity) {
                            case UNCOMMON:
                                shoeTypeButtonShadow.setImageResource(R.drawable.button_uncommon);
                                break;
                            case RARE:
                                shoeTypeButtonShadow.setImageResource(R.drawable.button_rare);
                                break;
                            case EPIC:
                                shoeTypeButtonShadow.setImageResource(R.drawable.button_epic);
                                break;
                            default:
                                shoeTypeButtonShadow.setImageResource(R.drawable.button_common);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        shoeTypeButton.setVisibility(View.VISIBLE);
                        shoeTypeLayout.setVisibility(View.VISIBLE);
                        shoeTypeButtonShadow.setImageResource(R.drawable.button_main_shadow);
                        break;
                }
                return false;
            }
        });

        shoeNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    shoeName = shoeNameEditText.getText().toString();
                }
            }
        });

        energyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    energyBox.setImageResource(R.drawable.box_energy_input_active);
                    oneTwentyFive = false;
                    oneTwentyFiveTextView.setText("100%");
                    energyEditText.setText(String.valueOf(energy));
                    energyEditText.selectAll();
                    InputMethodManager imm = (InputMethodManager) view.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, 0);
                } else {
                    energyBox.setImageResource(R.drawable.box_energy_input);
                    if (!energyEditText.getText().toString().isEmpty() && !energyEditText.getText().toString().equals(".")) {
                        if (Float.parseFloat(energyEditText.getText().toString()) < 0.2) {
                            energyEditText.setText("0.2");
                        } else if (Float.parseFloat(energyEditText.getText().toString()) > 25) {
                            energyEditText.setText("25");
                        }
                    } else {
                        energyEditText.setText("0");
                    }
                    energy = Float.parseFloat(energyEditText.getText().toString());
                    oneTwentyFiveEnergy = (float) (Math.round(energy * 12.5 - 0.5) / 10.0);
                    if (oneTwentyFiveEnergy * 10 % 2 != 0) {
                        oneTwentyFiveEnergy += 0.1;
                    }
                    calcTotals();
                }
            }
        });

        oneTwentyFiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFocus(view);
                oneTwentyFive = !oneTwentyFive;
                if (oneTwentyFive) {
                    oneTwentyFiveTextView.setText("125%");
                    if (oneTwentyFiveEnergy == 0) {
                        oneTwentyFiveEnergy = (float) (Math.round(energy * 12.5 - 0.5) / 10.0);
                        if (oneTwentyFiveEnergy * 10 % 2 != 0) {
                            oneTwentyFiveEnergy += 0.1;
                        }
                    }
                    energyEditText.setText(String.valueOf(oneTwentyFiveEnergy));
                } else {
                    oneTwentyFiveTextView.setText("100%");
                    energyEditText.setText(String.valueOf(energy));
                }
                calcTotals();
            }
        });

        levelSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                levelTextView.setText(String.valueOf(i + 1));
                if (i > 0) {
                    minLevelImageView.setVisibility(View.VISIBLE);
                } else {
                    minLevelImageView.setVisibility(View.INVISIBLE);
                }
                shoeLevel = i + 1;
                if (shoeLevel != 30 && gmtEarningOn) {
                    gmtEarningOn = false;
                    updateGmtSwitch();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateLevel();
            }
        });

        effEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!effEditText.getText().toString().isEmpty() && !effEditText.getText().toString().equals(".")) {
                        if (Float.parseFloat(effEditText.getText().toString()) < baseMin) {
                            baseEff = baseMin;
                            effEditText.setText(String.valueOf(baseMin));
                        } else if (Float.parseFloat(effEditText.getText().toString()) > baseMax) {
                            baseEff = baseMax;
                            effEditText.setText(String.valueOf(baseMax));
                        } else {
                            baseEff = Float.parseFloat(effEditText.getText().toString());
                        }
                    }
                    updatePoints();
                }
            }
        });

        luckEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!luckEditText.getText().toString().isEmpty() && !luckEditText.getText().toString().equals(".")) {
                        if (Float.parseFloat(luckEditText.getText().toString()) < baseMin) {
                            baseLuck = baseMin;
                            luckEditText.setText(String.valueOf(baseMin));
                        } else if (Float.parseFloat(luckEditText.getText().toString()) > baseMax) {
                            baseLuck = baseMax;
                            luckEditText.setText(String.valueOf(baseMax));
                        } else {
                            baseLuck = Float.parseFloat(luckEditText.getText().toString());
                        }
                    }

                    updatePoints();
                }
            }
        });

        comfortEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!comfortEditText.getText().toString().isEmpty() && !comfortEditText.getText().toString().equals(".")) {
                        if (Float.parseFloat(comfortEditText.getText().toString()) < baseMin) {
                            baseComf = baseMin;
                            comfortEditText.setText(String.valueOf(baseMin));
                        } else if (Float.parseFloat(comfortEditText.getText().toString()) > baseMax) {
                            baseComf = baseMax;
                            comfortEditText.setText(String.valueOf(baseMax));
                        } else {
                            baseComf = Float.parseFloat(comfortEditText.getText().toString());
                        }
                    }

                    updatePoints();
                }
            }
        });

        resEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!resEditText.getText().toString().isEmpty() && !resEditText.getText().toString().equals(".")) {
                        if (Float.parseFloat(resEditText.getText().toString()) < baseMin) {
                            baseRes = baseMin;
                            resEditText.setText(String.valueOf(baseMin));
                        } else if (Float.parseFloat(resEditText.getText().toString()) > baseMax) {
                            baseRes = baseMax;
                            resEditText.setText(String.valueOf(baseMax));
                        } else {
                            baseRes = Float.parseFloat(resEditText.getText().toString());
                        }
                    }

                    updatePoints();
                }
            }
        });

        // was going to make a function for all these damn buttons but without pointers I can't think
        // of how to do it :(

        subEffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (addedEff > 0) {
                    addedEff--;
                    pointsAvailable++;

                    effTotalTextView.setText(String.valueOf(baseEff + addedEff + gemEff));
                    updatePoints();
                    clearFocus(view);
                }
            }
        });

        subEffButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return false;
                }
                if (addedEff > 0) {
                    pointsAvailable += addedEff;
                    addedEff = 0;

                    effTotalTextView.setText(String.valueOf(baseEff + addedEff + gemEff));
                    updatePoints();
                    clearFocus(view);
                }
                return false;
            }
        });

        addEffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (pointsAvailable > 0) {
                    addedEff++;
                    pointsAvailable--;

                    effTotalTextView.setText(String.valueOf(baseEff + addedEff + gemEff));
                    updatePoints();
                    clearFocus(view);
                }
            }
        });

        addEffButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return false;
                }
                if (pointsAvailable > 0) {
                    addedEff += pointsAvailable;
                    pointsAvailable = 0;

                    effTotalTextView.setText(String.valueOf(baseEff + addedEff + gemEff));
                    updatePoints();
                    clearFocus(view);
                }
                return false;
            }
        });

        subLuckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (addedLuck > 0) {
                    addedLuck--;
                    pointsAvailable++;

                    luckTotalTextView.setText(String.valueOf(baseLuck + addedLuck + gemLuck));
                    updatePoints();
                    clearFocus(view);
                }
            }
        });

        subLuckButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return false;
                }
                if (addedLuck > 0) {
                    pointsAvailable += addedLuck;
                    addedLuck = 0;

                    luckTotalTextView.setText(String.valueOf(baseLuck + addedLuck + gemLuck));
                    updatePoints();
                    clearFocus(view);
                }
                return false;
            }
        });

        addLuckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (pointsAvailable > 0) {
                    addedLuck++;
                    pointsAvailable--;

                    luckTotalTextView.setText(String.valueOf(baseLuck + addedLuck + gemLuck));
                    updatePoints();
                    clearFocus(view);
                }
            }
        });

        addLuckButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return false;
                }
                if (pointsAvailable > 0) {
                    addedLuck += pointsAvailable;
                    pointsAvailable = 0;

                    luckTotalTextView.setText(String.valueOf(baseLuck + addedLuck + gemLuck));
                    updatePoints();
                    clearFocus(view);
                }
                return false;
            }
        });

        subComfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (addedComf > 0) {
                    addedComf--;
                    pointsAvailable++;

                    comfortTotalTextView.setText(String.valueOf(baseComf + addedComf + gemComf));
                    updatePoints();
                    clearFocus(view);
                }
            }
        });

        subComfButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return false;
                }
                if (addedComf > 0) {
                    pointsAvailable += addedComf;
                    addedComf = 0;

                    comfortTotalTextView.setText(String.valueOf(baseComf + addedComf + gemComf));
                    updatePoints();
                    clearFocus(view);
                }
                return false;
            }
        });

        addComfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (pointsAvailable > 0) {
                    addedComf++;
                    pointsAvailable--;

                    comfortTotalTextView.setText(String.valueOf(baseComf + addedComf + gemComf));
                    updatePoints();
                    clearFocus(view);
                }
            }
        });

        addComfButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return false;
                }
                if (pointsAvailable > 0) {
                    addedComf += pointsAvailable;
                    pointsAvailable = 0;

                    comfortTotalTextView.setText(String.valueOf(baseComf + addedComf + gemComf));
                    updatePoints();
                    clearFocus(view);
                }
                return false;
            }
        });

        subResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (addedRes > 0) {
                    addedRes--;
                    pointsAvailable++;
                    resTotalTextView.setText(String.valueOf(baseRes + addedRes + gemRes));
                    updatePoints();
                    clearFocus(view);
                }
            }
        });

        subResButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return false;
                }
                if (addedRes > 0) {
                    pointsAvailable += addedRes;
                    addedRes = 0;

                    resTotalTextView.setText(String.valueOf(baseRes + addedRes + gemRes));
                    updatePoints();
                    clearFocus(view);
                }
                return false;
            }
        });

        addResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (pointsAvailable > 0) {
                    addedRes++;
                    pointsAvailable--;

                    resTotalTextView.setText(String.valueOf(baseRes + addedRes + gemRes));
                    updatePoints();
                    clearFocus(view);
                }
            }
        });

        addResButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return false;
                }
                if (pointsAvailable > 0) {
                    addedRes += pointsAvailable;
                    pointsAvailable = 0;

                    resTotalTextView.setText(String.valueOf(baseRes + addedRes + gemRes));
                    updatePoints();
                    clearFocus(view);
                }
                return false;
            }
        });

        optimizeGstGmtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (energy > 0) {
                    if (gmtEarningOn) {
                        if (comfGemPrice == 0) {
                            Toast.makeText(getContext(), "For a more accurate estimation, enter comfort gem price", Toast.LENGTH_SHORT).show();
                        }
                        optimizeForGmt();
                    } else {
                        if (comfGemPrice != 0) {
                            optimizeForGst(true);
                        } else {
                            Toast.makeText(getContext(), "For a more accurate estimation, enter comfort gem price", Toast.LENGTH_SHORT).show();
                            optimizeForGst(false);
                        }

                    }
                } else if (baseEff == 0 || baseLuck == 0 || baseComf == 0 || baseRes == 0) {
                    Toast.makeText(getContext(), "Base values must be greater than 0", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Energy must be greater than 0", Toast.LENGTH_SHORT).show();
                }
                clearFocus(view);
            }
        });

        optimizeGstGmtButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (shoeLocked) {
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        optimizeGstGmtButton.setVisibility(View.INVISIBLE);
                        optimizeGstGmtTextView.setVisibility(View.INVISIBLE);
                        optimizeGstGmtButtonShadow.setImageResource(R.drawable.button_start);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        optimizeGstGmtButton.setVisibility(View.VISIBLE);
                        optimizeGstGmtTextView.setVisibility(View.VISIBLE);
                        optimizeGstGmtButtonShadow.setImageResource(R.drawable.button_start_shadow);
                        break;
                }
                return false;
            }
        });

        optimizeLuckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (energy > 0) {
                    if (comfGemPrice == 0) {
                        Toast.makeText(getContext(), "For a more accurate estimation, enter comfort gem price", Toast.LENGTH_SHORT).show();
                    }
                    if (gmtEarningOn) {
                        optimizeForLuckGmt();
                    } else {
                        optimizeForLuckGst();
                    }
                } else if (baseEff == 0 || baseLuck == 0 || baseComf == 0 || baseRes == 0) {
                    Toast.makeText(getContext(), "Base values must be greater than 0", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Energy must be greater than 0", Toast.LENGTH_SHORT).show();
                }
                clearFocus(view);
            }
        });

        optimizeLuckButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (shoeLocked) {
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        optimizeLuckButton.setVisibility(View.INVISIBLE);
                        optimizeLuckTextView.setVisibility(View.INVISIBLE);
                        optimizeLuckButtonShadow.setImageResource(R.drawable.button_start);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        optimizeLuckButton.setVisibility(View.VISIBLE);
                        optimizeLuckTextView.setVisibility(View.VISIBLE);
                        optimizeLuckButtonShadow.setImageResource(R.drawable.button_start_shadow);
                        break;
                }
                return false;
            }
        });

        changeComfGemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comfGemLvlForRepair < 3) {
                    comfGemLvlForRepair++;
                } else {
                    comfGemLvlForRepair = 1;
                }
                comfGemPrice = GEM_PRICES[comfGemLvlForRepair - 1];
                comfGemPriceEditText.setText(String.valueOf(comfGemPrice));
                updateHpRepairComfGem(comfGemHpRepairImageView);
                updateHpRepairComfGem(comfGemHpRepairTotalImageView);
                calcTotals();
            }
        });

        changeGmtEstimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeIncomeEst();
                clearFocus(view);
            }
        });

        changeGstLimitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGstLimit();
                clearFocus(view);
            }
        });

        toggleGstEstLimitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useGstLimit = !useGstLimit;
                calcTotals();
                clearFocus(view);
            }
        });

        chainSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (shoeChain == SOL) {
                    shoeChain = BSC;
                } else if (shoeChain == BSC) {
                    shoeChain = ETH;
                } else {
                    shoeChain = SOL;
                }
                updateChain();
                getGemPrices();
                clearFocus(view);
            }
        });

        chainSelectButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (shoeLocked) {
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        chainSelectButton.setVisibility(View.INVISIBLE);
                        chainSelectTv.setVisibility(View.INVISIBLE);
                        chainSelectShadowTv.setText(chainSelectTv.getText().toString());
                        switch (shoeChain) {
                            case BSC:
                                chainSelectShadow.setImageResource(R.drawable.button_bsc);
                                break;
                            case ETH:
                                chainSelectShadow.setImageResource(R.drawable.button_eth);
                                break;
                            default:
                                chainSelectShadow.setImageResource(R.drawable.button_sol);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        chainSelectButton.setVisibility(View.VISIBLE);
                        chainSelectTv.setVisibility(View.VISIBLE);
                        chainSelectShadow.setImageResource(R.drawable.button_main_shadow);
                        break;
                }
                return false;
            }
        });

        coinSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }
                if (shoeLevel != 30) {
                    Toast.makeText(getContext(), "GMT earning unlocked at level 30", Toast.LENGTH_SHORT).show();
                } else {
                    gmtEarningOn = !gmtEarningOn;
                }
                updateGmtSwitch();
                calcTotals();
                clearFocus(view);
            }
        });

        coinSelectButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (shoeLocked) {
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        coinSelectButton.setVisibility(View.INVISIBLE);
                        coinSelectTv.setVisibility(View.INVISIBLE);
                        coinSelectIcon.setVisibility(View.INVISIBLE);
                        coinShadowSelectTv.setText(coinSelectTv.getText().toString());
                        coinSelectShadow.setImageResource(gmtEarningOn ? R.drawable.button_gmt : R.drawable.button_gst);
                        coinSelectIconShadow.setImageResource(gmtEarningOn ? R.drawable.logo_gmt : R.drawable.logo_gst);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        coinSelectButton.setVisibility(View.VISIBLE);
                        coinSelectTv.setVisibility(View.VISIBLE);
                        coinSelectIcon.setVisibility(View.VISIBLE);
                        coinSelectShadow.setImageResource(R.drawable.button_main_shadow);
                        break;
                }
                return false;
            }
        });

        comfGemPriceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    gemPriceBox.setImageResource(R.drawable.box_gem_price_input_active);
                    comfGemPriceEditText.selectAll();
                    InputMethodManager imm = (InputMethodManager) view.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, 0);
                } else {
                    gemPriceBox.setImageResource(R.drawable.box_gem_price_input);
                    if (!comfGemPriceEditText.getText().toString().isEmpty() && !comfGemPriceEditText.getText().toString().equals(".")) {
                        comfGemPrice = Float.parseFloat(comfGemPriceEditText.getText().toString());
                    } else {
                        comfGemPrice = 0;
                    }
                    calcTotals();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoeLocked) {
                    shoeLockedAlert();
                    return;
                }

                Dialog dialog = new Dialog(requireActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.reset_alert_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageButton noButton = dialog.findViewById(R.id.noButton);
                ImageButton yesButton = dialog.findViewById(R.id.yesButton);

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetPage();
                        dialog.dismiss();
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        loadPoints();
        updateType();
        updateRarity();
        updateShoeNums();
        updateGmtSwitch();
        updateChain();
        calcTotals();
        updateShoeLockStatus();

        if (update) {
            update = false;

            Dialog updateDialog = new Dialog(getActivity());

            updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            updateDialog.setCancelable(true);
            updateDialog.setContentView(R.layout.update_dialog_optimizer);
            updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            updateDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            ImageButton nextButton = updateDialog.findViewById(R.id.nextButton);

            updateDialog.show();

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDialog.dismiss();
                }
            });
        }
        return view;
    }

    private void shoeLockedAlert() {
        Toast.makeText(getContext(), "Shoe locked", Toast.LENGTH_SHORT).show();
        ObjectAnimator scaler = ObjectAnimator.ofPropertyValuesHolder(
                shoeLockedImageView,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaler.setDuration(120);
        scaler.setRepeatCount(3);
        scaler.setRepeatMode(ValueAnimator.REVERSE);
        scaler.start();
    }

    // locks buttons/inputs for shoe
    private void updateShoeLockStatus() {
        shoeLockedImageView.setImageResource(shoeLocked ? R.mipmap.shoe_lock_closed : R.mipmap.shoe_lock_open);
        levelSeekbar.setEnabled(!shoeLocked);
        effEditText.setEnabled(!shoeLocked);
        luckEditText.setEnabled(!shoeLocked);
        comfortEditText.setEnabled(!shoeLocked);
        resEditText.setEnabled(!shoeLocked);

        if (shoeLocked) {
            effMinusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            effPlusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            luckMinusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            luckPlusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            comfMinusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            comfPlusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            resMinusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            resPlusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
        } else {
            updatePoints();
        }
    }

    // updates UI depending on shoe rarity
    private void updateRarity() {
        switch (shoeRarity) {
            case UNCOMMON:
                shoeCircles.setImageResource(R.drawable.circles_uncommon);
                shoeRarityTextView.setText("Uncommon");

                shoeRarityTextView.setTextColor(COLOR_WHITE);
                shoeRarityShadowTextView.setTextColor(COLOR_WHITE);
                shoeRarityButton.setImageResource(R.drawable.button_uncommon);

                shoeTypeTextView.setTextColor(COLOR_WHITE);
                shoeTypeShadowTextView.setTextColor(COLOR_WHITE);
                shoeTypeButton.setImageResource(R.drawable.button_uncommon);

                shoeNameEditText.setTextColor(COLOR_WHITE);
                shoeNameBoxImageView.setImageResource(R.drawable.box_name_uncommon);

                footOne.setColorFilter(COLOR_WHITE);
                footTwo.setColorFilter(COLOR_WHITE);
                footThree.setColorFilter(COLOR_WHITE);
                footOneShadow.setColorFilter(COLOR_WHITE);
                footTwoShadow.setColorFilter(COLOR_WHITE);
                footThreeShadow.setColorFilter(COLOR_WHITE);

                effEditText.setHint("8 - 21.6");
                luckEditText.setHint("8 - 21.6");
                comfortEditText.setHint("8 - 21.6");
                resEditText.setHint("8 - 21.6");
                baseMin = 8;
                baseMax = 21.6f;

                for (int i = 0; i < 4; i++) {
                    if (gems.get(i).getSocketRarity() > 2) {
                        gems.get(i).setSocketRarity(2);
                    }
                }
                break;
            case RARE:
                shoeCircles.setImageResource(R.drawable.circles_rare);
                shoeRarityTextView.setText("Rare");

                shoeRarityTextView.setTextColor(COLOR_WHITE);
                shoeRarityShadowTextView.setTextColor(COLOR_WHITE);
                shoeRarityButton.setImageResource(R.drawable.button_rare);

                shoeTypeTextView.setTextColor(COLOR_WHITE);
                shoeTypeShadowTextView.setTextColor(COLOR_WHITE);
                shoeTypeButton.setImageResource(R.drawable.button_rare);

                shoeNameEditText.setTextColor(COLOR_WHITE);
                shoeNameBoxImageView.setImageResource(R.drawable.box_name_rare);

                footOne.setColorFilter(COLOR_WHITE);
                footTwo.setColorFilter(COLOR_WHITE);
                footThree.setColorFilter(COLOR_WHITE);
                footOneShadow.setColorFilter(COLOR_WHITE);
                footTwoShadow.setColorFilter(COLOR_WHITE);
                footThreeShadow.setColorFilter(COLOR_WHITE);

                effEditText.setHint("15 - 42");
                luckEditText.setHint("15 - 42");
                comfortEditText.setHint("15 - 42");
                resEditText.setHint("15 - 42");
                baseMin = 15;
                baseMax = 42f;

                for (int i = 0; i < 4; i++) {
                    if (gems.get(i).getSocketRarity() > 3) {
                        gems.get(i).setSocketRarity(3);
                    }
                }
                break;
            case EPIC:
                shoeCircles.setImageResource(R.drawable.circles_epic);
                shoeRarityTextView.setText("Epic");

                shoeRarityTextView.setTextColor(COLOR_WHITE);
                shoeRarityShadowTextView.setTextColor(COLOR_WHITE);
                shoeRarityButton.setImageResource(R.drawable.button_epic);

                shoeTypeTextView.setTextColor(COLOR_WHITE);
                shoeTypeShadowTextView.setTextColor(COLOR_WHITE);
                shoeTypeButton.setImageResource(R.drawable.button_epic);

                shoeNameEditText.setTextColor(COLOR_WHITE);
                shoeNameBoxImageView.setImageResource(R.drawable.box_name_epic);

                footOne.setColorFilter(COLOR_WHITE);
                footTwo.setColorFilter(COLOR_WHITE);
                footThree.setColorFilter(COLOR_WHITE);
                footOneShadow.setColorFilter(COLOR_WHITE);
                footTwoShadow.setColorFilter(COLOR_WHITE);
                footThreeShadow.setColorFilter(COLOR_WHITE);

                effEditText.setHint("28 - 75.6");
                luckEditText.setHint("28 - 75.6");
                comfortEditText.setHint("28 - 75.6");
                resEditText.setHint("28 - 75.6");
                baseMin = 28;
                baseMax = 75.6f;
                break;
            default:
                shoeCircles.setImageResource(R.drawable.circles_common);
                shoeRarityTextView.setText("Common");

                shoeRarityTextView.setTextColor(COLOR_ALMOST_BLACK);
                shoeRarityShadowTextView.setTextColor(COLOR_ALMOST_BLACK);
                shoeRarityButton.setImageResource(R.drawable.button_common);

                shoeTypeTextView.setTextColor(COLOR_ALMOST_BLACK);
                shoeTypeShadowTextView.setTextColor(COLOR_ALMOST_BLACK);
                shoeTypeButton.setImageResource(R.drawable.button_common);

                shoeNameEditText.setTextColor(COLOR_ALMOST_BLACK);
                shoeNameBoxImageView.setImageResource(R.drawable.box_name_common);

                footOne.setColorFilter(COLOR_ALMOST_BLACK);
                footTwo.setColorFilter(COLOR_ALMOST_BLACK);
                footThree.setColorFilter(COLOR_ALMOST_BLACK);
                footOneShadow.setColorFilter(COLOR_ALMOST_BLACK);
                footTwoShadow.setColorFilter(COLOR_ALMOST_BLACK);
                footThreeShadow.setColorFilter(COLOR_ALMOST_BLACK);

                effEditText.setHint("1 - 10");
                luckEditText.setHint("1 - 10");
                comfortEditText.setHint("1 - 10");
                resEditText.setHint("1 - 10");
                baseMin = 1;
                baseMax = 10;

                for (int i = 0; i < 4; i++) {
                    if (gems.get(i).getSocketRarity() > 1) {
                        gems.get(i).setSocketRarity(1);
                    }
                }
        }

        // make sure bases are correct for shoe rarity
        if (baseEff < baseMin && baseEff != 0) {
            baseEff = baseMin;
            effEditText.setText(String.valueOf(baseEff));
        } else if (baseEff > baseMax) {
            baseEff = baseMax;
            effEditText.setText(String.valueOf(baseEff));
        }
        if (baseLuck < baseMin && baseLuck != 0) {
            baseLuck = baseMin;
            luckEditText.setText(String.valueOf(baseLuck));
        } else if (baseLuck > baseMax) {
            baseLuck = baseMax;
            luckEditText.setText(String.valueOf(baseLuck));
        }
        if (baseComf < baseMin && baseComf != 0) {
            baseComf = baseMin;
            comfortEditText.setText(String.valueOf(baseComf));
        } else if (baseComf > baseMax) {
            baseComf = baseMax;
            comfortEditText.setText(String.valueOf(baseComf));
        }
        if (baseRes < baseMin && baseRes != 0) {
            baseRes = baseMin;
            resEditText.setText(String.valueOf(baseRes));
        } else if (baseRes > baseMax) {
            baseRes = baseMax;
            resEditText.setText(String.valueOf(baseRes));
        }

        updateLevel();
    }

    // updates UI depending on shoe type
    private void updateType() {
        switch (shoeType) {
            case JOGGER:
                if (shoeImageUrl.isEmpty()) {
                    shoeTypeImageView.setImageResource(R.mipmap.shoe_jogger);
                }
                shoeTypeTextView.setText("Jogger");
                footOne.setImageResource(R.mipmap.footprint);
                footTwo.setVisibility(View.VISIBLE);
                footThree.setVisibility(View.GONE);
                footOneShadow.setImageResource(R.mipmap.footprint);
                footTwoShadow.setVisibility(View.VISIBLE);
                footThreeShadow.setVisibility(View.GONE);
                break;
            case RUNNER:
                if (shoeImageUrl.isEmpty()) {
                    shoeTypeImageView.setImageResource(R.mipmap.shoe_runner);
                }
                shoeTypeTextView.setText("Runner");
                footOne.setImageResource(R.mipmap.footprint);
                footTwo.setVisibility(View.VISIBLE);
                footThree.setVisibility(View.VISIBLE);
                footOneShadow.setImageResource(R.mipmap.footprint);
                footTwoShadow.setVisibility(View.VISIBLE);
                footThreeShadow.setVisibility(View.VISIBLE);
                break;
            case TRAINER:
                if (shoeImageUrl.isEmpty()) {
                    shoeTypeImageView.setImageResource(R.mipmap.shoe_trainer);
                }
                shoeTypeTextView.setText("Trainer");
                footOne.setImageResource(R.mipmap.trainer_t);
                footTwo.setVisibility(View.GONE);
                footThree.setVisibility(View.GONE);
                footOneShadow.setImageResource(R.mipmap.trainer_t);
                footTwoShadow.setVisibility(View.GONE);
                footThreeShadow.setVisibility(View.GONE);
                break;
            default:
                if (shoeImageUrl.isEmpty()) {
                    shoeTypeImageView.setImageResource(R.mipmap.shoe_walker);
                }
                shoeTypeTextView.setText("Walker");
                footOne.setImageResource(R.mipmap.footprint);
                footTwo.setVisibility(View.GONE);
                footThree.setVisibility(View.GONE);
                footOneShadow.setImageResource(R.mipmap.footprint);
                footTwoShadow.setVisibility(View.GONE);
                footThreeShadow.setVisibility(View.GONE);
        }
        if (!shoeImageUrl.isEmpty()) {
            Picasso.get().load(shoeImageUrl).into(shoeTypeImageView);
        }
    }

    // updates values depending on level
    private void updateLevel() {

        if (shoeLevel >= 5) {
            gemSocketOneLockPlus.setImageResource(gems.get(0).getGemImageSource());
            gemSocketOne.setImageResource(gems.get(0).getSocketImageSource());
            gemSocketOneLockPlus.setPadding(0, (int) (gems.get(0).getTopPadding() * dpScale + 0.5f), 0,
                    (int) (gems.get(0).getBottomPadding() * dpScale + 0.5f));
        } else {
            gemSocketOneLockPlus.setImageResource(R.drawable.gem_socket_lock);
            gemSocketOne.setImageResource(R.mipmap.gem_socket_gray_0);
            gemSocketOneLockPlus.setPadding(0, (int) (2 * dpScale + 0.5f), 0, (int) (2 * dpScale + 0.5f));
        }

        if (shoeLevel >= 10) {
            gemSocketTwoLockPlus.setImageResource(gems.get(1).getGemImageSource());
            gemSocketTwo.setImageResource(gems.get(1).getSocketImageSource());
            gemSocketTwoLockPlus.setPadding(0, (int) (gems.get(1).getTopPadding() * dpScale + 0.5f), 0,
                    (int) (gems.get(1).getBottomPadding() * dpScale + 0.5f));
        } else {
            gemSocketTwoLockPlus.setImageResource(R.drawable.gem_socket_lock);
            gemSocketTwo.setImageResource(R.mipmap.gem_socket_gray_0);
            gemSocketTwoLockPlus.setPadding(0, (int) (2 * dpScale + 0.5f), 0, (int) (2 * dpScale + 0.5f));
        }

        if (shoeLevel >= 15) {
            gemSocketThreeLockPlus.setImageResource(gems.get(2).getGemImageSource());
            gemSocketThree.setImageResource(gems.get(2).getSocketImageSource());
            gemSocketThreeLockPlus.setPadding(0, (int) (gems.get(2).getTopPadding() * dpScale + 0.5f), 0,
                    (int) (gems.get(2).getBottomPadding() * dpScale + 0.5f));
        } else {
            gemSocketThreeLockPlus.setImageResource(R.drawable.gem_socket_lock);
            gemSocketThree.setImageResource(R.mipmap.gem_socket_gray_0);
            gemSocketThreeLockPlus.setPadding(0, (int) (2 * dpScale + 0.5f), 0, (int) (2 * dpScale + 0.5f));
        }

        if (shoeLevel >= 20) {
            gemSocketFourLockPlus.setImageResource(gems.get(3).getGemImageSource());
            gemSocketFour.setImageResource(gems.get(3).getSocketImageSource());
            gemSocketFourLockPlus.setPadding(0, (int) (gems.get(3).getTopPadding() * dpScale + 0.5f), 0,
                    (int) (gems.get(3).getBottomPadding() * dpScale + 0.5f));
        } else {
            gemSocketFourLockPlus.setImageResource(R.drawable.gem_socket_lock);
            gemSocketFour.setImageResource(R.mipmap.gem_socket_gray_0);
            gemSocketFourLockPlus.setPadding(0, (int) (2 * dpScale + 0.5f), 0, (int) (2 * dpScale + 0.5f));
        }

        if (shoeLevel < 10) {
            gstLimit = 5 + (shoeLevel * 5);
        } else if (shoeLevel < 23) {
            gstLimit = 60 + ((shoeLevel - 10) * 10);
        } else {
            gstLimit = 195 + ((shoeLevel - 23) * 15);
        }

        gstLimitTextView.setText(String.valueOf(gstLimit));
        updatePoints();
    }

    // dialog for choosing socket and gem
    private void chooseSocketType(final int socketNum) {
        final int tempSocketType, tempSocketRarity, tempGemMounted;
        final float tempBasePoints;

        saveNewGem = false;

        tempSocketType = gems.get(socketNum).getSocketType();
        tempSocketRarity = gems.get(socketNum).getSocketRarity();
        tempGemMounted = gems.get(socketNum).getMountedGem();
        tempBasePoints = gems.get(socketNum).getBasePoints();

        Dialog choseGem = new Dialog(requireActivity());

        choseGem.requestWindowFeature(Window.FEATURE_NO_TITLE);
        choseGem.setCancelable(true);
        choseGem.setContentView(R.layout.choose_gem_dialog);
        choseGem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        choseGem.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        choseGem.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!saveNewGem) {
                    gems.get(socketNum).setSocketType(tempSocketType);
                    gems.get(socketNum).setSocketRarity(tempSocketRarity);
                    gems.get(socketNum).setMountedGem(tempGemMounted);
                    gems.get(socketNum).setBasePoints(tempBasePoints);
                }
            }
        });

        ImageButton effTypeButton = choseGem.findViewById(R.id.effType);
        ImageButton luckTypeButton = choseGem.findViewById(R.id.luckType);
        ImageButton comfTypeButton = choseGem.findViewById(R.id.comfType);
        ImageButton resTypeButton = choseGem.findViewById(R.id.resType);

        ImageView effTypeSelected = choseGem.findViewById(R.id.effTypeSelected);
        ImageView luckTypeSelected = choseGem.findViewById(R.id.luckTypeSelected);
        ImageView comfTypeSelected = choseGem.findViewById(R.id.comfTypeSelected);
        ImageView resTypeSelected = choseGem.findViewById(R.id.resTypeSelected);

        ImageButton lvl1SelectedButton = choseGem.findViewById(R.id.gemLevel1Selected);
        ImageButton lvl2SelectedButton = choseGem.findViewById(R.id.gemLevel2Selected);
        ImageButton lvl3SelectedButton = choseGem.findViewById(R.id.gemLevel3Selected);
        ImageButton lvl4SelectedButton = choseGem.findViewById(R.id.gemLevel4Selected);
        ImageButton lvl5SelectedButton = choseGem.findViewById(R.id.gemLevel5Selected);
        ImageButton lvl6SelectedButton = choseGem.findViewById(R.id.gemLevel6Selected);
        ImageButton lvl7SelectedButton = choseGem.findViewById(R.id.gemLevel7Selected);
        ImageButton lvl8SelectedButton = choseGem.findViewById(R.id.gemLevel8Selected);

        ImageView lvl1Gem = choseGem.findViewById(R.id.gemLevel1);
        ImageView lvl2Gem = choseGem.findViewById(R.id.gemLevel2);
        ImageView lvl3Gem = choseGem.findViewById(R.id.gemLevel3);
        ImageView lvl4Gem = choseGem.findViewById(R.id.gemLevel4);
        ImageView lvl5Gem = choseGem.findViewById(R.id.gemLevel5);
        ImageView lvl6Gem = choseGem.findViewById(R.id.gemLevel6);
        ImageView lvl7Gem = choseGem.findViewById(R.id.gemLevel7);
        ImageView lvl8Gem = choseGem.findViewById(R.id.gemLevel8);

        ImageButton gemSocket = choseGem.findViewById(R.id.gemSocket);
        ImageView gemSocketPlus = choseGem.findViewById(R.id.socketPlus);

        TextView decreaseRarityTextView = choseGem.findViewById(R.id.minusTextView);
        TextView increaseRarityTextView = choseGem.findViewById(R.id.plusTextView);
        Button decreaseRarityButton = choseGem.findViewById(R.id.decreaseRarityButton);
        Button increaseRarityButton = choseGem.findViewById(R.id.increaseRarityButton);
        Button showCalcsButton = choseGem.findViewById(R.id.seeCalcsButton);

        TextView gemDetailsTextView = choseGem.findViewById(R.id.gemDetailsTextView);
        TextView socketDetailsTextView = choseGem.findViewById(R.id.socketDetailsTextView);
        TextView totalGemPointsTextView = choseGem.findViewById(R.id.totalPointsTextView);

        ImageButton saveButton = choseGem.findViewById(R.id.saveGemButton);

        ArrayList<ImageButton> selectedButtons = new ArrayList<>();

        selectedButtons.add(lvl1SelectedButton);
        selectedButtons.add(lvl2SelectedButton);
        selectedButtons.add(lvl3SelectedButton);
        selectedButtons.add(lvl4SelectedButton);
        selectedButtons.add(lvl5SelectedButton);
        selectedButtons.add(lvl6SelectedButton);
        selectedButtons.add(lvl7SelectedButton);
        selectedButtons.add(lvl8SelectedButton);

        gemSocket.setImageResource(gems.get(socketNum).getSocketImageSource());

        switch (tempSocketType) {
            case EFF:
                gems.get(socketNum).setBasePoints(baseEff);

                effTypeSelected.setVisibility(View.VISIBLE);
                luckTypeSelected.setVisibility(View.INVISIBLE);
                comfTypeSelected.setVisibility(View.INVISIBLE);
                resTypeSelected.setVisibility(View.INVISIBLE);

                lvl1Gem.setImageResource(R.mipmap.gem_eff_level1);
                lvl2Gem.setImageResource(R.mipmap.gem_eff_level2);
                lvl3Gem.setImageResource(R.mipmap.gem_eff_level3);
                lvl4Gem.setImageResource(R.mipmap.gem_eff_level4);
                lvl5Gem.setImageResource(R.mipmap.gem_eff_level5);
                lvl6Gem.setImageResource(R.mipmap.gem_eff_level6);
                lvl7Gem.setImageResource(R.mipmap.gem_eff_level7);
                lvl8Gem.setImageResource(R.mipmap.gem_eff_level8);

                for (byte i = 0; i < 8; i++) {
                    selectedButtons.get(i).setImageResource(R.drawable.circles_eff);
                }
                break;
            case LUCK:
                gems.get(socketNum).setBasePoints(baseLuck);

                effTypeSelected.setVisibility(View.INVISIBLE);
                luckTypeSelected.setVisibility(View.VISIBLE);
                comfTypeSelected.setVisibility(View.INVISIBLE);
                resTypeSelected.setVisibility(View.INVISIBLE);

                lvl1Gem.setImageResource(R.mipmap.gem_luck_level1);
                lvl2Gem.setImageResource(R.mipmap.gem_luck_level2);
                lvl3Gem.setImageResource(R.mipmap.gem_luck_level3);
                lvl4Gem.setImageResource(R.mipmap.gem_luck_level4);
                lvl5Gem.setImageResource(R.mipmap.gem_luck_level5);
                lvl6Gem.setImageResource(R.mipmap.gem_luck_level6);
                lvl7Gem.setImageResource(R.mipmap.gem_luck_level7);
                lvl8Gem.setImageResource(R.mipmap.gem_luck_level8);

                for (byte i = 0; i < 8; i++) {
                    selectedButtons.get(i).setImageResource(R.drawable.circles_luck);
                }
                break;
            case COMF:
                gems.get(socketNum).setBasePoints(baseComf);

                effTypeSelected.setVisibility(View.INVISIBLE);
                luckTypeSelected.setVisibility(View.INVISIBLE);
                comfTypeSelected.setVisibility(View.VISIBLE);
                resTypeSelected.setVisibility(View.INVISIBLE);

                lvl1Gem.setImageResource(R.mipmap.gem_comf_level1);
                lvl2Gem.setImageResource(R.mipmap.gem_comf_level2);
                lvl3Gem.setImageResource(R.mipmap.gem_comf_level3);
                lvl4Gem.setImageResource(R.mipmap.gem_comf_level4);
                lvl5Gem.setImageResource(R.mipmap.gem_comf_level5);
                lvl6Gem.setImageResource(R.mipmap.gem_comf_level6);
                lvl7Gem.setImageResource(R.mipmap.gem_comf_level7);
                lvl8Gem.setImageResource(R.mipmap.gem_comf_level8);

                for (byte i = 0; i < 8; i++) {
                    selectedButtons.get(i).setImageResource(R.drawable.circles_comf);
                }
                break;
            case RES:
                gems.get(socketNum).setBasePoints(baseRes);

                effTypeSelected.setVisibility(View.INVISIBLE);
                luckTypeSelected.setVisibility(View.INVISIBLE);
                comfTypeSelected.setVisibility(View.INVISIBLE);
                resTypeSelected.setVisibility(View.VISIBLE);

                lvl1Gem.setImageResource(R.mipmap.gem_res_level1);
                lvl2Gem.setImageResource(R.mipmap.gem_res_level2);
                lvl3Gem.setImageResource(R.mipmap.gem_res_level3);
                lvl4Gem.setImageResource(R.mipmap.gem_res_level4);
                lvl5Gem.setImageResource(R.mipmap.gem_res_level5);
                lvl6Gem.setImageResource(R.mipmap.gem_res_level6);
                lvl7Gem.setImageResource(R.mipmap.gem_res_level7);
                lvl8Gem.setImageResource(R.mipmap.gem_res_level8);

                for (byte i = 0; i < 8; i++) {
                    selectedButtons.get(i).setImageResource(R.drawable.circles_res);
                }
                break;
            default:
                gems.get(socketNum).setBasePoints(0);

                effTypeSelected.setVisibility(View.INVISIBLE);
                luckTypeSelected.setVisibility(View.INVISIBLE);
                comfTypeSelected.setVisibility(View.INVISIBLE);
                resTypeSelected.setVisibility(View.INVISIBLE);

                lvl1Gem.setImageResource(R.mipmap.gem_grey_level1);
                lvl2Gem.setImageResource(R.mipmap.gem_grey_level2);
                lvl3Gem.setImageResource(R.mipmap.gem_grey_level3);
                lvl4Gem.setImageResource(R.mipmap.gem_grey_level4);
                lvl5Gem.setImageResource(R.mipmap.gem_grey_level5);
                lvl6Gem.setImageResource(R.mipmap.gem_grey_level6);
                lvl7Gem.setImageResource(R.mipmap.gem_grey_level7);
                lvl8Gem.setImageResource(R.mipmap.gem_grey_level8);

                for (byte i = 0; i < 8; i++) {
                    selectedButtons.get(i).setImageResource(R.drawable.circles_common);
                }
        }

        switch (tempGemMounted) {
            case 1:
                lvl1SelectedButton.setAlpha(1.0f);
                lvl2SelectedButton.setAlpha(0.0f);
                lvl3SelectedButton.setAlpha(0.0f);
                lvl4SelectedButton.setAlpha(0.0f);
                lvl5SelectedButton.setAlpha(0.0f);
                lvl6SelectedButton.setAlpha(0.0f);
                lvl7SelectedButton.setAlpha(0.0f);
                lvl8SelectedButton.setAlpha(0.0f);
                gemSocketPlus.setPadding(0, (int) (3 * dpScale + 0.5f), 0, (int) (3 * dpScale + 0.5f));
                break;
            case 2:
                lvl1SelectedButton.setAlpha(0.0f);
                lvl2SelectedButton.setAlpha(1.0f);
                lvl3SelectedButton.setAlpha(0.0f);
                lvl4SelectedButton.setAlpha(0.0f);
                lvl5SelectedButton.setAlpha(0.0f);
                lvl6SelectedButton.setAlpha(0.0f);
                lvl7SelectedButton.setAlpha(0.0f);
                lvl8SelectedButton.setAlpha(0.0f);
                break;
            case 3:
                lvl1SelectedButton.setAlpha(0.0f);
                lvl2SelectedButton.setAlpha(0.0f);
                lvl3SelectedButton.setAlpha(1.0f);
                lvl4SelectedButton.setAlpha(0.0f);
                lvl5SelectedButton.setAlpha(0.0f);
                lvl6SelectedButton.setAlpha(0.0f);
                lvl7SelectedButton.setAlpha(0.0f);
                lvl8SelectedButton.setAlpha(0.0f);
                break;
            case 4:
                lvl1SelectedButton.setAlpha(0.0f);
                lvl2SelectedButton.setAlpha(0.0f);
                lvl3SelectedButton.setAlpha(0.0f);
                lvl4SelectedButton.setAlpha(1.0f);
                lvl5SelectedButton.setAlpha(0.0f);
                lvl6SelectedButton.setAlpha(0.0f);
                lvl7SelectedButton.setAlpha(0.0f);
                lvl8SelectedButton.setAlpha(0.0f);
                gemSocketPlus.setPadding(0, (int) (2 * dpScale + 0.5f), 0, 0);
                break;
            case 5:
                lvl1SelectedButton.setAlpha(0.0f);
                lvl2SelectedButton.setAlpha(0.0f);
                lvl3SelectedButton.setAlpha(0.0f);
                lvl4SelectedButton.setAlpha(0.0f);
                lvl5SelectedButton.setAlpha(1.0f);
                lvl6SelectedButton.setAlpha(0.0f);
                lvl7SelectedButton.setAlpha(0.0f);
                lvl8SelectedButton.setAlpha(0.0f);
                gemSocketPlus.setPadding(0, (int) (2 * dpScale + 0.5f), 0, 0);
                break;
            case 6:
                lvl1SelectedButton.setAlpha(0.0f);
                lvl2SelectedButton.setAlpha(0.0f);
                lvl3SelectedButton.setAlpha(0.0f);
                lvl4SelectedButton.setAlpha(0.0f);
                lvl5SelectedButton.setAlpha(0.0f);
                lvl6SelectedButton.setAlpha(1.0f);
                lvl7SelectedButton.setAlpha(0.0f);
                lvl8SelectedButton.setAlpha(0.0f);
                break;
            case 7:
                lvl1SelectedButton.setAlpha(0.0f);
                lvl2SelectedButton.setAlpha(0.0f);
                lvl3SelectedButton.setAlpha(0.0f);
                lvl4SelectedButton.setAlpha(0.0f);
                lvl5SelectedButton.setAlpha(0.0f);
                lvl6SelectedButton.setAlpha(0.0f);
                lvl7SelectedButton.setAlpha(1.0f);
                lvl8SelectedButton.setAlpha(0.0f);
                break;
            case 8:
                lvl1SelectedButton.setAlpha(0.0f);
                lvl2SelectedButton.setAlpha(0.0f);
                lvl3SelectedButton.setAlpha(0.0f);
                lvl4SelectedButton.setAlpha(0.0f);
                lvl5SelectedButton.setAlpha(0.0f);
                lvl6SelectedButton.setAlpha(0.0f);
                lvl7SelectedButton.setAlpha(0.0f);
                lvl8SelectedButton.setAlpha(1.0f);
                break;
            default:
                lvl1SelectedButton.setAlpha(0.0f);
                lvl2SelectedButton.setAlpha(0.0f);
                lvl3SelectedButton.setAlpha(0.0f);
                lvl4SelectedButton.setAlpha(0.0f);
                lvl5SelectedButton.setAlpha(0.0f);
                lvl6SelectedButton.setAlpha(0.0f);
                lvl7SelectedButton.setAlpha(0.0f);
                lvl8SelectedButton.setAlpha(0.0f);
                break;
        }

        gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());

        if (tempSocketRarity == 0) {
            decreaseRarityTextView.setTextColor(COLOR_GEM_SOCKET_SHADOW);
        } else if ((tempSocketRarity == 1 && shoeRarity <= COMMON)
                || (tempSocketRarity == 2 && shoeRarity <= UNCOMMON)
                || (tempSocketRarity == 3 && shoeRarity <= RARE)
                || tempSocketRarity == 4) {
            increaseRarityTextView.setTextColor(COLOR_GEM_SOCKET_SHADOW);
        }

        gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
        socketDetailsTextView.setText(gems.get(socketNum).getSocketParamsString());
        totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());

        choseGem.show();

        effTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                effTypeSelected.setVisibility(View.VISIBLE);
                luckTypeSelected.setVisibility(View.INVISIBLE);
                comfTypeSelected.setVisibility(View.INVISIBLE);
                resTypeSelected.setVisibility(View.INVISIBLE);

                lvl1Gem.setImageResource(R.mipmap.gem_eff_level1);
                lvl2Gem.setImageResource(R.mipmap.gem_eff_level2);
                lvl3Gem.setImageResource(R.mipmap.gem_eff_level3);
                lvl4Gem.setImageResource(R.mipmap.gem_eff_level4);
                lvl5Gem.setImageResource(R.mipmap.gem_eff_level5);
                lvl6Gem.setImageResource(R.mipmap.gem_eff_level6);
                lvl7Gem.setImageResource(R.mipmap.gem_eff_level7);
                lvl8Gem.setImageResource(R.mipmap.gem_eff_level8);

                for (byte i = 0; i < 8; i++) {
                    selectedButtons.get(i).setImageResource(R.drawable.circles_eff);
                }

                gems.get(socketNum).setSocketType(EFF);
                gems.get(socketNum).setBasePoints(baseEff);
                gemSocket.setImageResource(gems.get(socketNum).getSocketImageSource());
                gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                socketDetailsTextView.setText(gems.get(socketNum).getSocketParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        luckTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                effTypeSelected.setVisibility(View.INVISIBLE);
                luckTypeSelected.setVisibility(View.VISIBLE);
                comfTypeSelected.setVisibility(View.INVISIBLE);
                resTypeSelected.setVisibility(View.INVISIBLE);

                lvl1Gem.setImageResource(R.mipmap.gem_luck_level1);
                lvl2Gem.setImageResource(R.mipmap.gem_luck_level2);
                lvl3Gem.setImageResource(R.mipmap.gem_luck_level3);
                lvl4Gem.setImageResource(R.mipmap.gem_luck_level4);
                lvl5Gem.setImageResource(R.mipmap.gem_luck_level5);
                lvl6Gem.setImageResource(R.mipmap.gem_luck_level6);
                lvl7Gem.setImageResource(R.mipmap.gem_luck_level7);
                lvl8Gem.setImageResource(R.mipmap.gem_luck_level8);

                for (byte i = 0; i < 8; i++) {
                    selectedButtons.get(i).setImageResource(R.drawable.circles_luck);
                }

                gems.get(socketNum).setSocketType(LUCK);
                gems.get(socketNum).setBasePoints(baseLuck);
                gemSocket.setImageResource(gems.get(socketNum).getSocketImageSource());
                gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                socketDetailsTextView.setText(gems.get(socketNum).getSocketParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        comfTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                effTypeSelected.setVisibility(View.INVISIBLE);
                luckTypeSelected.setVisibility(View.INVISIBLE);
                comfTypeSelected.setVisibility(View.VISIBLE);
                resTypeSelected.setVisibility(View.INVISIBLE);

                lvl1Gem.setImageResource(R.mipmap.gem_comf_level1);
                lvl2Gem.setImageResource(R.mipmap.gem_comf_level2);
                lvl3Gem.setImageResource(R.mipmap.gem_comf_level3);
                lvl4Gem.setImageResource(R.mipmap.gem_comf_level4);
                lvl5Gem.setImageResource(R.mipmap.gem_comf_level5);
                lvl6Gem.setImageResource(R.mipmap.gem_comf_level6);
                lvl7Gem.setImageResource(R.mipmap.gem_comf_level7);
                lvl8Gem.setImageResource(R.mipmap.gem_comf_level8);

                for (byte i = 0; i < 8; i++) {
                    selectedButtons.get(i).setImageResource(R.drawable.circles_comf);
                }

                gems.get(socketNum).setSocketType(COMF);
                gems.get(socketNum).setBasePoints(baseComf);
                gemSocket.setImageResource(gems.get(socketNum).getSocketImageSource());
                gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                socketDetailsTextView.setText(gems.get(socketNum).getSocketParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        resTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                effTypeSelected.setVisibility(View.INVISIBLE);
                luckTypeSelected.setVisibility(View.INVISIBLE);
                comfTypeSelected.setVisibility(View.INVISIBLE);
                resTypeSelected.setVisibility(View.VISIBLE);

                lvl1Gem.setImageResource(R.mipmap.gem_res_level1);
                lvl2Gem.setImageResource(R.mipmap.gem_res_level2);
                lvl3Gem.setImageResource(R.mipmap.gem_res_level3);
                lvl4Gem.setImageResource(R.mipmap.gem_res_level4);
                lvl5Gem.setImageResource(R.mipmap.gem_res_level5);
                lvl6Gem.setImageResource(R.mipmap.gem_res_level6);
                lvl7Gem.setImageResource(R.mipmap.gem_res_level7);
                lvl8Gem.setImageResource(R.mipmap.gem_res_level8);

                for (byte i = 0; i < 8; i++) {
                    selectedButtons.get(i).setImageResource(R.drawable.circles_res);
                }

                gems.get(socketNum).setSocketType(RES);
                gems.get(socketNum).setBasePoints(baseRes);
                gemSocket.setImageResource(gems.get(socketNum).getSocketImageSource());
                gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                socketDetailsTextView.setText(gems.get(socketNum).getSocketParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        decreaseRarityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getSocketRarity() > 0) {
                    gems.get(socketNum).setSocketRarity(gems.get(socketNum).getSocketRarity() - 1);
                    gemSocket.setImageResource(gems.get(socketNum).getSocketImageSource());
                    increaseRarityTextView.setTextColor(COLOR_ALMOST_BLACK);
                }
                if (gems.get(socketNum).getSocketRarity() == 0) {
                    decreaseRarityTextView.setTextColor(COLOR_GEM_SOCKET_SHADOW);
                }
                socketDetailsTextView.setText(gems.get(socketNum).getSocketParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        increaseRarityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getSocketRarity() == 0) {
                    gems.get(socketNum).setSocketRarity(1);
                    if (shoeRarity == COMMON) {
                        increaseRarityTextView.setTextColor(COLOR_GEM_SOCKET_SHADOW);
                    }
                } else if (gems.get(socketNum).getSocketRarity() == 1 && shoeRarity > COMMON) {
                    gems.get(socketNum).setSocketRarity(2);
                    if (shoeRarity <= UNCOMMON) {
                        increaseRarityTextView.setTextColor(COLOR_GEM_SOCKET_SHADOW);
                    }
                } else if (gems.get(socketNum).getSocketRarity() == 2 && shoeRarity > UNCOMMON) {
                    gems.get(socketNum).setSocketRarity(3);
                    if (shoeRarity <= RARE) {
                        increaseRarityTextView.setTextColor(COLOR_GEM_SOCKET_SHADOW);
                    }
                } else if (gems.get(socketNum).getSocketRarity() == 3 && shoeRarity > RARE) {
                    gems.get(socketNum).setSocketRarity(4);
                    increaseRarityTextView.setTextColor(COLOR_GEM_SOCKET_SHADOW);
                }

                if (gems.get(socketNum).getSocketRarity() == 0) {
                    decreaseRarityTextView.setTextColor(COLOR_GEM_SOCKET_SHADOW);
                } else {
                    decreaseRarityTextView.setTextColor(COLOR_ALMOST_BLACK);
                }

                gemSocket.setImageResource(gems.get(socketNum).getSocketImageSource());
                socketDetailsTextView.setText(gems.get(socketNum).getSocketParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewGem = true;
                choseGem.dismiss();
                updateLevel();
            }
        });

        lvl1SelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getMountedGem() == 1) {
                    gems.get(socketNum).setMountedGem(0);
                    gemSocketPlus.setImageResource(R.drawable.gem_socket_plus);
                    gemSocketPlus.setPadding((int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f));
                    lvl1SelectedButton.setAlpha(0.0f);
                } else {
                    gems.get(socketNum).setMountedGem(1);
                    gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());
                    gemSocketPlus.setPadding(0, (int) (4 * dpScale + 0.5f), 0, (int) (4 * dpScale + 0.5f));

                    lvl1SelectedButton.setAlpha(1.0f);
                    lvl2SelectedButton.setAlpha(0.0f);
                    lvl3SelectedButton.setAlpha(0.0f);
                    lvl4SelectedButton.setAlpha(0.0f);
                    lvl5SelectedButton.setAlpha(0.0f);
                    lvl6SelectedButton.setAlpha(0.0f);
                    lvl7SelectedButton.setAlpha(0.0f);
                    lvl8SelectedButton.setAlpha(0.0f);
                }

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        lvl2SelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getMountedGem() == 2) {
                    gems.get(socketNum).setMountedGem(0);
                    gemSocketPlus.setImageResource(R.drawable.gem_socket_plus);
                    gemSocketPlus.setPadding((int) (3 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (3 * dpScale + 0.5f), (int) (3 * dpScale + 0.5f));
                    lvl2SelectedButton.setAlpha(0.0f);
                } else {
                    gems.get(socketNum).setMountedGem(2);
                    gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());
                    gemSocketPlus.setPadding(0,0,0,0);

                    lvl1SelectedButton.setAlpha(0.0f);
                    lvl2SelectedButton.setAlpha(1.0f);
                    lvl3SelectedButton.setAlpha(0.0f);
                    lvl4SelectedButton.setAlpha(0.0f);
                    lvl5SelectedButton.setAlpha(0.0f);
                    lvl6SelectedButton.setAlpha(0.0f);
                    lvl7SelectedButton.setAlpha(0.0f);
                    lvl8SelectedButton.setAlpha(0.0f);
                }

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        lvl3SelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getMountedGem() == 3) {
                    gems.get(socketNum).setMountedGem(0);
                    gemSocketPlus.setImageResource(R.drawable.gem_socket_plus);
                    gemSocketPlus.setPadding((int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f));
                    lvl3SelectedButton.setAlpha(0.0f);
                } else {
                    gems.get(socketNum).setMountedGem(3);
                    gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());
                    gemSocketPlus.setPadding(0,0,0,(int) (1 * dpScale + 0.5f));

                    lvl1SelectedButton.setAlpha(0.0f);
                    lvl2SelectedButton.setAlpha(0.0f);
                    lvl3SelectedButton.setAlpha(1.0f);
                    lvl4SelectedButton.setAlpha(0.0f);
                    lvl5SelectedButton.setAlpha(0.0f);
                    lvl6SelectedButton.setAlpha(0.0f);
                    lvl7SelectedButton.setAlpha(0.0f);
                    lvl8SelectedButton.setAlpha(0.0f);
                }

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        lvl4SelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getMountedGem() == 4) {
                    gems.get(socketNum).setMountedGem(0);
                    gemSocketPlus.setImageResource(R.drawable.gem_socket_plus);
                    gemSocketPlus.setPadding((int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f));
                    lvl4SelectedButton.setAlpha(0.0f);
                } else {
                    gems.get(socketNum).setMountedGem(4);
                    gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());
                    gemSocketPlus.setPadding(0, (int) (2 * dpScale + 0.5f), 0, 0);

                    lvl1SelectedButton.setAlpha(0.0f);
                    lvl2SelectedButton.setAlpha(0.0f);
                    lvl3SelectedButton.setAlpha(0.0f);
                    lvl4SelectedButton.setAlpha(1.0f);
                    lvl5SelectedButton.setAlpha(0.0f);
                    lvl6SelectedButton.setAlpha(0.0f);
                    lvl7SelectedButton.setAlpha(0.0f);
                    lvl8SelectedButton.setAlpha(0.0f);
                }

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        lvl5SelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getMountedGem() == 5) {
                    gems.get(socketNum).setMountedGem(0);
                    gemSocketPlus.setImageResource(R.drawable.gem_socket_plus);
                    gemSocketPlus.setPadding((int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f));
                    lvl5SelectedButton.setAlpha(0.0f);
                } else {
                    gems.get(socketNum).setMountedGem(5);
                    gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());
                    gemSocketPlus.setPadding(0, (int) (2 * dpScale + 0.5f), 0, 0);

                    lvl1SelectedButton.setAlpha(0.0f);
                    lvl2SelectedButton.setAlpha(0.0f);
                    lvl3SelectedButton.setAlpha(0.0f);
                    lvl4SelectedButton.setAlpha(0.0f);
                    lvl5SelectedButton.setAlpha(1.0f);
                    lvl6SelectedButton.setAlpha(0.0f);
                    lvl7SelectedButton.setAlpha(0.0f);
                    lvl8SelectedButton.setAlpha(0.0f);
                }

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        lvl6SelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getMountedGem() == 6) {
                    gems.get(socketNum).setMountedGem(0);
                    gemSocketPlus.setImageResource(R.drawable.gem_socket_plus);
                    gemSocketPlus.setPadding((int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f));
                    lvl6SelectedButton.setAlpha(0.0f);
                } else {
                    gems.get(socketNum).setMountedGem(6);
                    gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());
                    gemSocketPlus.setPadding(0,0,0,1);

                    lvl1SelectedButton.setAlpha(0.0f);
                    lvl2SelectedButton.setAlpha(0.0f);
                    lvl3SelectedButton.setAlpha(0.0f);
                    lvl4SelectedButton.setAlpha(0.0f);
                    lvl5SelectedButton.setAlpha(0.0f);
                    lvl6SelectedButton.setAlpha(1.0f);
                    lvl7SelectedButton.setAlpha(0.0f);
                    lvl8SelectedButton.setAlpha(0.0f);
                }

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        lvl7SelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getMountedGem() == 7) {
                    gems.get(socketNum).setMountedGem(0);
                    gemSocketPlus.setImageResource(R.drawable.gem_socket_plus);
                    gemSocketPlus.setPadding((int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f));
                    lvl7SelectedButton.setAlpha(0.0f);
                } else {
                    gems.get(socketNum).setMountedGem(7);
                    gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());
                    gemSocketPlus.setPadding(0,0,0,1);

                    lvl1SelectedButton.setAlpha(0.0f);
                    lvl2SelectedButton.setAlpha(0.0f);
                    lvl3SelectedButton.setAlpha(0.0f);
                    lvl4SelectedButton.setAlpha(0.0f);
                    lvl5SelectedButton.setAlpha(0.0f);
                    lvl6SelectedButton.setAlpha(0.0f);
                    lvl7SelectedButton.setAlpha(1.0f);
                    lvl8SelectedButton.setAlpha(0.0f);
                }

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        lvl8SelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getMountedGem() == 8) {
                    gems.get(socketNum).setMountedGem(0);
                    gemSocketPlus.setImageResource(R.drawable.gem_socket_plus);
                    gemSocketPlus.setPadding((int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f));
                    lvl8SelectedButton.setAlpha(0.0f);
                } else {
                    gems.get(socketNum).setMountedGem(8);
                    gemSocketPlus.setImageResource(gems.get(socketNum).getGemImageSource());
                    gemSocketPlus.setPadding(0,0,0,1);

                    lvl1SelectedButton.setAlpha(0.0f);
                    lvl2SelectedButton.setAlpha(0.0f);
                    lvl3SelectedButton.setAlpha(0.0f);
                    lvl4SelectedButton.setAlpha(0.0f);
                    lvl5SelectedButton.setAlpha(0.0f);
                    lvl6SelectedButton.setAlpha(0.0f);
                    lvl7SelectedButton.setAlpha(0.0f);
                    lvl8SelectedButton.setAlpha(1.0f);
                }

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        gemSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gems.get(socketNum).getMountedGem() == 0) {
                    Toast.makeText(getContext(), "Select a gem to mount", Toast.LENGTH_SHORT).show();
                } else {
                    gems.get(socketNum).setMountedGem(0);
                    gemSocketPlus.setImageResource(R.drawable.gem_socket_plus);
                    gemSocketPlus.setPadding((int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f), (int) (2 * dpScale + 0.5f));

                    lvl1SelectedButton.setAlpha(0.0f);
                    lvl2SelectedButton.setAlpha(0.0f);
                    lvl3SelectedButton.setAlpha(0.0f);
                    lvl4SelectedButton.setAlpha(0.0f);
                    lvl5SelectedButton.setAlpha(0.0f);
                    lvl6SelectedButton.setAlpha(0.0f);
                    lvl7SelectedButton.setAlpha(0.0f);
                    lvl8SelectedButton.setAlpha(0.0f);
                }

                gemDetailsTextView.setText(gems.get(socketNum).getGemParamsString());
                totalGemPointsTextView.setText(gems.get(socketNum).getTotalPointsString());
            }
        });

        showCalcsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGemCalcs(socketNum);
            }
        });
    }

    // shows detailed gem calculations
    private void showGemCalcs(final int socketNum) {
        int points, percent;
        String socketRarity;

        Dialog showGemCalcDetails = new Dialog(requireActivity());

        showGemCalcDetails.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showGemCalcDetails.setCancelable(true);
        showGemCalcDetails.setContentView(R.layout.gem_calcs_dialog);
        showGemCalcDetails.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showGemCalcDetails.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView calcsSocket = showGemCalcDetails.findViewById(R.id.seeCalcsSocketImageView);
        ImageView calcsGem = showGemCalcDetails.findViewById(R.id.seeCalcsGemImageView);

        TextView calcsGemLvl = showGemCalcDetails.findViewById(R.id.seeCalcsTitleGemTextView);
        TextView calcsGemInfo = showGemCalcDetails.findViewById(R.id.seeCalcsGemAddedPointsTextView);
        TextView calcsGemCalcs = showGemCalcDetails.findViewById(R.id.seeCalcsGemTotalCalcTextView);

        TextView calcsSocketRarity = showGemCalcDetails.findViewById(R.id.seeCalcsSocketTextView);
        TextView calcsSocketInfo = showGemCalcDetails.findViewById(R.id.seeCalcsSocketMultiplierTextView);
        TextView calcsSocketCalcs = showGemCalcDetails.findViewById(R.id.seeCalcsSocketTotalCalcTextView);
        TextView calcsTotalPoints = showGemCalcDetails.findViewById(R.id.seeCalcsTotalTextView);

        showGemCalcDetails.show();

        switch (gems.get(socketNum).getMountedGem()) {
            case 1:
                points = 2;
                percent = 5;
                break;
            case 2:
                points = 8;
                percent = 70;
                break;
            case 3:
                points = 25;
                percent = 220;
                break;
            case 4:
                points = 72;
                percent = 600;
                break;
            case 5:
                points = 200;
                percent = 1400;
                break;
            case 6:
                points = 400;
                percent = 4300;
                break;
            case 7:
                points = 777;
                percent = 11000;
                break;
            case 8:
                points = 1888;
                percent = 28000;
                break;
            default:
                points = 0;
                percent = 0;
                break;
        }

        switch (gems.get(socketNum).getSocketRarity()) {
            case 1:
                socketRarity = getString(R.string.uncommon_socket);
                break;
            case 2:
                socketRarity = getString(R.string.rare_socket);
                break;
            case 3:
                socketRarity = getString(R.string.epic_socket);
                break;
            case 4:
                socketRarity = getString(R.string.legendary_socket);
                break;
            default:
                socketRarity = getString(R.string.common_socket);
        }

        String gemLevel = getString(R.string.level) + " " + gems.get(socketNum).getMountedGem() + getString(R.string.gem);
        String gemInfo = "+ " + percent + getString(R.string.base_percent) + points + getString(R.string.points);
        String gemCalcs = "(" + gems.get(socketNum).getBasePoints() + " × " + percent + "%) + "
                + points + " = " + gems.get(socketNum).getGemParams();
        String socketInfo = getString(R.string.gem_points) + gems.get(socketNum).getSocketParamsString();
        String socketCalcs = gems.get(socketNum).getGemParams() + " × " + gems.get(socketNum).getSocketParams()
                + " = " + gems.get(socketNum).getTotalPoints();

        calcsSocket.setImageResource(gems.get(socketNum).getSocketImageSource());
        calcsGem.setImageResource(gems.get(socketNum).getGemImageSource());
        calcsGem.setPadding(0, (int) (gems.get(socketNum).getTopPadding() * dpScale + 0.5f), 0,
                (int) (gems.get(socketNum).getBottomPadding() * dpScale + 0.5f));

        calcsGemLvl.setText(gemLevel);
        calcsGemInfo.setText(gemInfo);
        calcsGemCalcs.setText(gemCalcs);

        calcsSocketRarity.setText(socketRarity);
        calcsSocketInfo.setText(socketInfo);
        calcsSocketCalcs.setText(socketCalcs);
        calcsTotalPoints.setText(gems.get(socketNum).getTotalPointsString());

    }

    // gives option to increase daily GST limit (allows for daily earning estimates based on actual limit)
    private void updateGstLimit() {
        // reset gst limit to default val
        int tempLimit;
        if (shoeLevel < 10) {
            tempLimit = 5 + (shoeLevel * 5);
        } else if (shoeLevel < 23) {
            tempLimit = 60 + ((shoeLevel - 10) * 10);
        } else {
            tempLimit = 195 + ((shoeLevel - 23) * 15);
        }
        if (gstLimit != tempLimit) {
            gstLimit = tempLimit;
        }

        Dialog updateGstLimitDialog = new Dialog(requireActivity());

        updateGstLimitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateGstLimitDialog.setCancelable(true);
        updateGstLimitDialog.setContentView(R.layout.update_gst_limit_dialog);
        updateGstLimitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateGstLimitDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        TextView localGstLimitTextView = updateGstLimitDialog.findViewById(R.id.gstLimitTextView);
        SeekBar gstSeekBar = updateGstLimitDialog.findViewById(R.id.gstSeekBar);
        ImageView gstMinPercent = updateGstLimitDialog.findViewById(R.id.seekbarMinGst);
        TextView gmtCostTv = updateGstLimitDialog.findViewById(R.id.gmtCostTextView);
        ImageButton saveButton = updateGstLimitDialog.findViewById(R.id.saveButton);

        localGstLimitTextView.setText(String.valueOf(gstLimit + 20));

        updateGstLimitDialog.show();

        gstSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int gmtCost = 0;
                if (i < 5) {
                    for (int j = 0; j < i + 1; j++) {
                        gmtCost += 60 * (5 + (2 * j));
                    }
                }
                final int cost = i < 5 ? gmtCost : 4600 + ((i - 6) * 1000);
                localGstLimitTextView.setText(String.valueOf((i + 1) * 20 + gstLimit));
                gmtCostTv.setText(String.valueOf(cost));
                if (i > 0) {
                    gstMinPercent.setVisibility(View.VISIBLE);
                } else {
                    gstMinPercent.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGstLimitDialog.dismiss();
                gstLimit = Integer.parseInt(localGstLimitTextView.getText().toString());
                gstLimitTextView.setText(String.valueOf(gstLimit));
                calcTotals();
            }
        });
    }

    // gives option to change income estimate (allows for more accurate USD price predictions)
    private void changeIncomeEst() {
        final float gmtIncome = Float.parseFloat(gmtTotalTv.getText().toString());
        final int[] tempPercent = {gmtPercentModifier};
        final String percent = gmtPercentModifier + "%";

        Dialog changeIncomeDialog = new Dialog(requireActivity());

        changeIncomeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeIncomeDialog.setCancelable(true);
        changeIncomeDialog.setContentView(R.layout.change_income_dialog);
        changeIncomeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeIncomeDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        TextView gmtIncomeTextView = changeIncomeDialog.findViewById(R.id.gmtIncomeTextView);
        SeekBar gmtSeekbar = changeIncomeDialog.findViewById(R.id.gmtSeekBar);
        ImageView gmtMinPercent = changeIncomeDialog.findViewById(R.id.seekbarMinGmt);
        TextView gmtPercent = changeIncomeDialog.findViewById(R.id.gmtPercentLabelTextView);
        ImageButton saveButton = changeIncomeDialog.findViewById(R.id.saveButton);

        gmtIncomeTextView.setText(gmtTotalTv.getText().toString());
        gmtPercent.setText(percent);

        changeIncomeDialog.show();

        gmtSeekbar.setProgress(gmtPercentModifier);

        gmtSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                final String percent = i + 1 + "%";
                final double income = Math.round((i + 1) * gmtIncome) / 100.0;
                tempPercent[0] = i + 1;
                gmtIncomeTextView.setText(String.valueOf(income));
                gmtPercent.setText(percent);
                if (i > 52) {
                    gmtMinPercent.setVisibility(View.VISIBLE);
                } else {
                    gmtMinPercent.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeIncomeDialog.dismiss();
                gmtPercentModifier = tempPercent[0];
                calcTotals();
            }
        });
    }

    // dialog to update shoe image given a url
    private void changeShoeImage() {
        Dialog changeImageDialog = new Dialog(requireActivity());

        changeImageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeImageDialog.setCancelable(true);
        changeImageDialog.setContentView(R.layout.change_shoe_image_dialog);
        changeImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeImageDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        EditText imageUrlEditText = changeImageDialog.findViewById(R.id.urlEditText);
        ImageButton saveButton = changeImageDialog.findViewById(R.id.saveButton);

        if (!shoeImageUrl.isEmpty()) {
            imageUrlEditText.setText(shoeImageUrl);
        }

        changeImageDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImageDialog.dismiss();
                shoeImageUrl = imageUrlEditText.getText().toString();
                if (!shoeImageUrl.isEmpty()) {
                    Picasso.get().load(shoeImageUrl).into(shoeTypeImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            shoeTypeImageView.setScaleX(1.1f);
                            shoeTypeImageView.setScaleY(1.1f);
                            ObjectAnimator scaler = ObjectAnimator.ofPropertyValuesHolder(
                                    shoeTypeImageView,
                                    PropertyValuesHolder.ofFloat("scaleX", 1f),
                                    PropertyValuesHolder.ofFloat("scaleY", 1f));
                            scaler.setDuration(1000);
                            scaler.start();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(requireActivity(), "Error loading image - check URL and try again", Toast.LENGTH_SHORT).show();
                            shoeImageUrl = "";
                            updateType();
                        }
                    });
                } else {
                    updateType();
                }
            }
        });
    }

    // returns estimated durability lost
    private int getDurabilityLost(double localEnergy, double totalRes) {
        int durLost;
        if (totalRes > 160) {
            durLost = (int) Math.round(localEnergy * (-0.0005 * totalRes + 0.4));
        } else {
            durLost = (int) Math.round(localEnergy * ((2.944 * Math.exp(-totalRes / 6.763)) + (2.119 * Math.exp(-totalRes / 36.817)) + 0.294));
        }
        return Math.max(durLost, 1);
    }

    private double getGstTotal(double localEnergy, double totalEff) {
        switch (shoeType) {
            case JOGGER:
                return Math.floor(localEnergy * Math.pow(totalEff, 0.48) * 10) / 10;
            case RUNNER:
                return Math.floor(localEnergy * Math.pow(totalEff, 0.49) * 10) / 10;
            case TRAINER:
                return Math.floor(localEnergy * Math.pow(totalEff, 0.492) * 10) / 10;
            default:
                return Math.floor(localEnergy * Math.pow(totalEff, 0.47) * 10) / 10;
        }
    }

    // returns estimated gmt per energy
    private double getGmtPerEnergy(double totalComf) {
        double gmtPerEnergy = gmtNumA * Math.pow(totalComf, gmtNumB) - gmtNumC;

        switch (shoeType) {
            case JOGGER:
                break;
            case RUNNER:
                gmtPerEnergy *= 1.02;
                break;
            case TRAINER:
                gmtPerEnergy *= 1.025;
                break;
            default:
                gmtPerEnergy *= 0.98;
        }

        if (gmtPercentModifier != 100) {
            gmtPerEnergy = gmtPerEnergy * (gmtPercentModifier / 100.0);
            gmtTotalTv.setTextColor(COLOR_PROGRESS_GMT);
        } else {
            gmtTotalTv.setTextColor(COLOR_ALMOST_BLACK);
        }

        return gmtPerEnergy;
    }

    // calculate earnings
    private void calcTotals() {
        if (baseEff == 0 || baseLuck == 0 || baseComf == 0 || baseRes == 0) {
            estGstGmtTextView.setText("0");
            durabilityLossTextView.setText("0");
            repairCostDurTextView.setText("0");
            hpLossTextView.setText("0");
            repairCostHpTextView.setText("0");
            gemMultipleTextView.setText("0");
            gemMultipleTotalTextView.setText("- 0");
            totalIncomeTextView.setText("0");
            gmtTotalTv.setText("0");
            totalIncomeUsdTextView.setText("0");
            clearMbs();
            return;
        }

        final float localEnergy = (oneTwentyFive ? oneTwentyFiveEnergy : energy);

        double repairCostDurability, gstGmtTotal, hpRatio, repairCostHp, gstProfitBeforeGem, totalUsd;
        double gmtLowRange = 0;
        double gmtHighRange = 0;
        double totalEff = Float.parseFloat(effTotalTextView.getText().toString());
        double totalComf = Float.parseFloat(comfortTotalTextView.getText().toString());
        double totalRes = Float.parseFloat(resTotalTextView.getText().toString());

        if (gmtEarningOn) {
            gstGmtTotal = getGmtPerEnergy(totalComf);
            gmtLowRange = Math.max(0, Math.round((gstGmtTotal - 0.2f) * 20) / 100.0);
            gmtHighRange = Math.round((gstGmtTotal + 0.2f) * 20) / 100.0;
            gstGmtTotal = Math.max(0, gstGmtTotal * localEnergy);
        } else {
            gstGmtTotal = getGstTotal(localEnergy, totalEff);
        }

        repairCostDurability = getRepairCost() * getDurabilityLost(localEnergy, totalRes);
        hpCalcs((float) totalComf);

        hpRatio = hpLoss / hpPercentRestored;
        comfGemMultiplier = (float) (hpLoss / hpPercentRestored);

        repairCostHp = gstCostBasedOnGem * hpRatio;
        calcMbChances();

        repairCostHp = Math.round(repairCostHp * 10.0) / 10.0;
        repairCostDurability = Math.round(repairCostDurability * 10.0) / 10.0;

        if (hpLoss == 0) {
            hpLossTextView.setText("0");
            repairCostHpTextView.setText("0");
            gemMultipleTextView.setText("0");
            gemMultipleTotalTextView.setText("- 0");
        } else {
            hpLossTextView.setText(String.valueOf(Math.round(hpLoss * 100) / 100.0));
            repairCostHpTextView.setText(String.valueOf(repairCostHp));
            gemMultipleTextView.setText(String.valueOf(Math.round(hpRatio * 100.0) / 100.0));
            String multiplier = "- " + Math.round(hpRatio * 100.0) / 100.0;
            gemMultipleTotalTextView.setText(multiplier);
        }

        durabilityLossTextView.setText(String.valueOf(getDurabilityLost(localEnergy, totalRes)));
        repairCostDurTextView.setText(String.valueOf(repairCostDurability));

        if (gmtEarningOn) {
            String gmtRange = gmtLowRange + " - " + gmtHighRange;
            totalUsd = Math.round(((gstGmtTotal * TOKEN_PRICES[0]) - ((repairCostDurability + repairCostHp) * TOKEN_PRICES[shoeChain + 1])
                    - (comfGemMultiplier * comfGemPrice * TOKEN_PRICES[0])) * 100) / 100.0;
            estGstGmtTextView.setText(gmtRange);
            totalIncomeTextView.setText(String.valueOf(Math.round((repairCostDurability + repairCostHp) * 10.0) / 10.0));
            gmtTotalTv.setText(String.valueOf(Math.round(gstGmtTotal * 10) / 10.0));
            estGstGmtTextView.setTextColor(COLOR_ALMOST_BLACK);
        } else {
            if (useGstLimit) {
                gstGmtTotal = Math.min(gstGmtTotal, gstLimit);
            }
            gstProfitBeforeGem = gstGmtTotal - repairCostDurability - repairCostHp;
            totalUsd = Math.round(((gstProfitBeforeGem * TOKEN_PRICES[shoeChain + 1]) - (comfGemMultiplier * comfGemPrice * TOKEN_PRICES[0])) * 100) / 100.0;
            estGstGmtTextView.setText(String.valueOf(gstGmtTotal));
            totalIncomeTextView.setText(String.valueOf(Math.round(gstProfitBeforeGem * 10) / 10.0));
            if (getGstTotal(localEnergy, totalEff) > gstLimit) {
                estGstGmtTextView.setTextColor(COLOR_RED);
            } else {
                estGstGmtTextView.setTextColor(COLOR_ALMOST_BLACK);
            }
        }

        if (comfGemPrice == 0) {
            totalIncomeUsdTextView.setText(R.string.enter_gem_price);
            totalIncomeUsdTextView.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto_condensed_regular));
            totalIncomeUsdTextView.setTextColor(COLOR_GANDALF);
            totalIncomeUsdTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        } else {
            totalIncomeUsdTextView.setText(String.format("%.2f", totalUsd));
            totalIncomeUsdTextView.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto_condensed_bold_italic));
            totalIncomeUsdTextView.setTextColor(COLOR_ALMOST_BLACK);
            totalIncomeUsdTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        }
    }

    // finds the point allocation that is most profitable for GST
    private void optimizeForGst(boolean usdOn) {
        final float localEnergy = (oneTwentyFive ? oneTwentyFiveEnergy : energy);

        double profit;
        int optimalAddedEff = 0;
        int optimalAddedRes = 0;
        int optimalAddedComf = 0;

        final int localPoints = shoeLevel * 2 * shoeRarity;
        double maxProfit = -50;

        float localEff = baseEff + gemEff;
        float localComf = baseComf + gemComf;
        float localRes = baseRes + gemRes;
        int localAddedEff = 0;
        int localAddedComf = 0;
        int localAddedRes;

        // O(n^2) w/ max 45,150 calcs... yikes
        while (localAddedEff <= localPoints) {
            while (localAddedComf <= localPoints - localAddedEff) {
                localAddedRes = localPoints - localAddedComf - localAddedEff;

                hpCalcs(localComf + localAddedComf);
                comfGemMultiplier = (float) (hpLoss / hpPercentRestored);

                profit = getGstTotal(localEnergy, localEff + localAddedEff)
                        - (getDurabilityLost(localEnergy, localRes + localAddedRes) * getRepairCost())
                        - (gstCostBasedOnGem * (hpLoss / hpPercentRestored));

                if (usdOn) {
                    profit = ((profit * TOKEN_PRICES[shoeChain + 1]) - (comfGemMultiplier * comfGemPrice * TOKEN_PRICES[0]));
                }
                if (profit > maxProfit) {
                    optimalAddedEff = localAddedEff;
                    optimalAddedComf = localAddedComf;
                    optimalAddedRes = localAddedRes;
                    maxProfit = profit;
                }

                localAddedComf ++;
            }
            localAddedComf = 0;
            localAddedEff++;
        }

        addedEff = optimalAddedEff;
        addedRes = optimalAddedRes;
        addedLuck = 0;
        addedComf = optimalAddedComf;
        updatePoints();
    }

    // finds the point allocation that is most profitable for GMT
    private void optimizeForGmt() {
        final float localEnergy = (oneTwentyFive ? oneTwentyFiveEnergy : energy);

        double profit;
        int optimalAddedRes = 0;
        int optimalAddedComf = 0;

        final int localPoints = shoeLevel * 2 * shoeRarity;
        double maxProfit = -50;

        float localComf = baseComf + gemComf;
        float localRes = baseRes + gemRes;
        int localAddedComf = 0;
        int localAddedRes;

        while (localAddedComf <= localPoints) {
            localAddedRes = localPoints - localAddedComf;
            hpCalcs(localComf + localAddedComf);

            // total profit GMT in USD
            profit = localEnergy * getGmtPerEnergy(localComf + localAddedComf) * TOKEN_PRICES[0];
            // subtract USD cost of repair durability restore HP (GST)
            profit -= (TOKEN_PRICES[shoeChain + 1]) * (getDurabilityLost(localEnergy, localRes + localAddedRes) * getRepairCost());
            profit -= (TOKEN_PRICES[shoeChain + 1]) * (gstCostBasedOnGem * (hpLoss / hpPercentRestored));
            // subtract USD cost of restore HP
            profit -= ((hpLoss / hpPercentRestored) * comfGemPrice * TOKEN_PRICES[0]);

            if (profit > maxProfit) {
                optimalAddedComf = localAddedComf;
                optimalAddedRes = localAddedRes;
                maxProfit = profit;
            }

            localAddedComf ++;
        }

        addedRes = optimalAddedRes;
        addedLuck = 0;
        addedEff = 0;
        addedComf = optimalAddedComf;
        updatePoints();
    }

    // optimizes for most luck with no GST loss
    private void optimizeForLuckGst() {
        final int localPoints = shoeLevel * 2 * shoeRarity;

        if (breakEvenGst(0, 0, 0)) {
            addedEff = 0;
            addedLuck = localPoints;
            addedComf = 0;
            addedRes = 0;
            updatePoints();
            return;
        }

        int localAddedEff = 1;
        int localAddedComf = 0;
        int localAddedRes = 0;
        int pointsSpent = 1;
        boolean zero = false;

        while (pointsSpent <= localPoints) {
            if (breakEvenGst(localAddedEff, localAddedComf, localAddedRes)) {
                zero = true;
                break;
            }

            while (localAddedEff > 0) {
                localAddedEff--;
                localAddedComf++;

                if (breakEvenGst(localAddedEff, localAddedComf, localAddedRes)) {
                    zero = true;
                    break;
                }

                while (localAddedComf > 0) {
                    localAddedComf--;
                    localAddedRes++;

                    if (breakEvenGst(localAddedEff, localAddedComf, localAddedRes)) {
                        zero = true;
                        break;
                    }
                }

                if (zero) {
                    break;
                }

                localAddedComf += localAddedRes;
                localAddedRes = 0;
            }

            if (zero) {
                break;
            }

            pointsSpent++;
            localAddedEff = pointsSpent;
            localAddedComf = 0;
        }

        if (zero) {
            addedEff = localAddedEff;
            addedRes = localAddedRes;
            addedLuck = localPoints - pointsSpent;
            addedComf = localAddedComf;
        } else {
            Toast.makeText(requireActivity(), "Cannot optimize luck - shoe always loses money", Toast.LENGTH_SHORT).show();
        }
        updatePoints();
    }

    // optimizes for most luck with no GMT loss
    private void optimizeForLuckGmt() {
        final int localPoints = shoeLevel * 2 * shoeRarity;

        if (breakEvenGmt(0, 0)) {
            addedEff = 0;
            addedLuck = localPoints;
            addedComf = 0;
            addedRes = 0;
            updatePoints();
            return;
        }

        int localAddedComf = 1;
        int localAddedRes = 0;
        int pointsSpent = 1;
        boolean zero = false;

        while (localAddedComf <= localPoints) {
            if (breakEvenGmt(localAddedComf, localAddedRes)) {
                zero = true;
                break;
            }

            while (localAddedComf > 0) {
                localAddedComf--;
                localAddedRes++;

                if (breakEvenGmt(localAddedComf, localAddedRes)) {
                    zero = true;
                    break;
                }
            }

            if (zero) {
                break;
            }

            pointsSpent++;
            localAddedComf = pointsSpent;
            localAddedRes = 0;
        }

        if (zero) {
            addedEff = 0;
            addedRes = localAddedRes;
            addedLuck = localPoints - pointsSpent;
            addedComf = localAddedComf;
        } else {
            Toast.makeText(requireActivity(), "Cannot optimize luck - shoe always loses money", Toast.LENGTH_SHORT).show();
        }
        updatePoints();
    }

    // check GST profit, returns true if greater than 0
    private boolean breakEvenGst(final int localAddedEff, final int localAddedComf, final int localAddedRes) {
        final float localEnergy = (oneTwentyFive ? oneTwentyFiveEnergy : energy);
        float localEff = baseEff + gemEff;
        float localComf = baseComf + gemComf;
        float localRes = baseRes + gemRes;
        double profit;

        hpCalcs(localComf + localAddedComf);

        // gst profit
        profit = getGstTotal(localEnergy, localEff + localAddedEff);
        // minus repair cost
        profit -= (getDurabilityLost(localEnergy, localRes + localAddedRes) * getRepairCost());
        // minus restore cost
        profit -= (gstCostBasedOnGem * (hpLoss / hpPercentRestored));

        // in USD
        profit = (profit * TOKEN_PRICES[shoeChain + 1]);
        // minus gem restore cost
        profit -= ((hpLoss / hpPercentRestored) * comfGemPrice * TOKEN_PRICES[0]);

        return profit > 0;
    }

    private boolean breakEvenGmt(final int localAddedComf, final int localAddedRes) {
        final float localEnergy = (oneTwentyFive ? oneTwentyFiveEnergy : energy);
        float localComf = baseComf + gemComf;
        float localRes = baseRes + gemRes;
        double profit;

        hpCalcs(localComf + localAddedComf);

        // total profit GMT in USD
        profit = localEnergy * getGmtPerEnergy(localComf + localAddedComf) * TOKEN_PRICES[0];
        // subtract USD cost of repair durability and restore HP
        profit -= (TOKEN_PRICES[shoeChain + 1]) * (getDurabilityLost(localEnergy, localRes + localAddedRes) * getRepairCost());
        profit -= (TOKEN_PRICES[shoeChain + 1]) * (gstCostBasedOnGem * (hpLoss / hpPercentRestored));
        // subtract USD cost of restore HP
        profit -= ((hpLoss / hpPercentRestored) * comfGemPrice * TOKEN_PRICES[0]);
        return profit > 0;
    }

    // sets up HP calcs based on gem level and shoe rarity
    private void hpCalcs(final float totalComf) {
        final float localEnergy = (oneTwentyFive ? oneTwentyFiveEnergy : energy);

        switch (comfGemLvlForRepair) {
            case 2:
                gstCostBasedOnGem = 30;
                break;
            case 3:
                gstCostBasedOnGem = 100;
                break;
            default:
                gstCostBasedOnGem = 10;
        }

        switch (shoeRarity) {
            case COMMON:
                hpLoss = localEnergy * 0.386 * Math.pow(totalComf, -0.421);
                switch (comfGemLvlForRepair) {
                    case 2:
                        hpPercentRestored = 39;
                        break;
                    case 3:
                        hpPercentRestored = 100;
                        break;
                    default:
                        hpPercentRestored = 3;
                }
                break;
            case UNCOMMON:
                hpLoss = localEnergy * 0.424 * Math.pow(totalComf, -0.456);
                switch (comfGemLvlForRepair) {
                    case 2:
                        hpPercentRestored = 23;
                        break;
                    case 3:
                        hpPercentRestored = 100;
                        break;
                    default:
                        hpPercentRestored = 1.8f;
                }
                break;
            case RARE:
                hpLoss = localEnergy * 0.47 * Math.pow(totalComf, -0.467);
                switch (comfGemLvlForRepair) {
                    case 2:
                        hpPercentRestored = 16;
                        break;
                    case 3:
                        hpPercentRestored = 92;
                        break;
                    default:
                        hpPercentRestored = 1.2f;
                }
                break;
            case EPIC:
                hpLoss = localEnergy * 0.47 * Math.pow(totalComf, -0.467);
                switch (comfGemLvlForRepair) {
                    case 2:
                        hpPercentRestored = 11;
                        break;
                    case 3:
                        hpPercentRestored = 67;
                        break;
                    default:
                        hpPercentRestored = 0.88f;
                }
                break;
            default:
                hpLoss = 0;
                hpPercentRestored = 1;
        }

        if (totalComf == 0 || localEnergy == 0) {
            hpLoss = 0;
            hpPercentRestored = 1;
        }
    }

    // returns base repair cost in gst based on shoe rarity and level
    // consider making this a formula in the future...
    private float getRepairCost() {
        float baseCost;

        if (shoeRarity == COMMON) {
            switch (shoeLevel) {
                case 1:
                    baseCost = 0.31f;
                    break;
                case 2:
                    baseCost = 0.32f;
                    break;
                case 3:
                    baseCost = 0.33f;
                    break;
                case 4:
                    baseCost = 0.35f;
                    break;
                case 5:
                    baseCost = 0.36f;
                    break;
                case 6:
                    baseCost = 0.37f;
                    break;
                case 7:
                    baseCost = 0.38f;
                    break;
                case 8:
                    baseCost = 0.4f;
                    break;
                case 9:
                    baseCost = 0.41f;
                    break;
                case 10:
                    baseCost = 0.42f;
                    break;
                case 11:
                    baseCost = 0.44f;
                    break;
                case 12:
                    baseCost = 0.46f;
                    break;
                case 13:
                    baseCost = 0.48f;
                    break;
                case 14:
                    baseCost = 0.5f;
                    break;
                case 15:
                    baseCost = 0.52f;
                    break;
                case 16:
                    baseCost = 0.54f;
                    break;
                case 17:
                    baseCost = 0.56f;
                    break;
                case 18:
                    baseCost = 0.58f;
                    break;
                case 19:
                    baseCost = 0.6f;
                    break;
                case 20:
                    baseCost = 0.62f;
                    break;
                case 21:
                    baseCost = 0.64f;
                    break;
                case 22:
                    baseCost = 0.67f;
                    break;
                case 23:
                    baseCost = 0.7f;
                    break;
                case 24:
                    baseCost = 0.72f;
                    break;
                case 25:
                    baseCost = 0.75f;
                    break;
                case 26:
                    baseCost = 0.78f;
                    break;
                case 27:
                    baseCost = 0.81f;
                    break;
                case 28:
                    baseCost = 0.83f;
                    break;
                case 29:
                    baseCost = 0.87f;
                    break;
                case 30:
                    baseCost = 0.9f;
                    break;
                default:
                    baseCost = 0;
            }
        } else if (shoeRarity == UNCOMMON) {
            switch (shoeLevel) {
                case 1:
                    baseCost = 0.41f;
                    break;
                case 2:
                    baseCost = 0.43f;
                    break;
                case 3:
                    baseCost = 0.45f;
                    break;
                case 4:
                    baseCost = 0.46f;
                    break;
                case 5:
                    baseCost = 0.48f;
                    break;
                case 6:
                    baseCost = 0.5f;
                    break;
                case 7:
                    baseCost = 0.51f;
                    break;
                case 8:
                    baseCost = 0.53f;
                    break;
                case 9:
                    baseCost = 0.55f;
                    break;
                case 10:
                    baseCost = 0.57f;
                    break;
                case 11:
                    baseCost = 0.6f;
                    break;
                case 12:
                    baseCost = 0.62f;
                    break;
                case 13:
                    baseCost = 0.64f;
                    break;
                case 14:
                    baseCost = 0.66f;
                    break;
                case 15:
                    baseCost = 0.69f;
                    break;
                case 16:
                    baseCost = 0.71f;
                    break;
                case 17:
                    baseCost = 0.74f;
                    break;
                case 18:
                    baseCost = 0.77f;
                    break;
                case 19:
                    baseCost = 0.8f;
                    break;
                case 20:
                    baseCost = 0.83f;
                    break;
                case 21:
                    baseCost = 0.86f;
                    break;
                case 22:
                    baseCost = 0.89f;
                    break;
                case 23:
                    baseCost = 0.92f;
                    break;
                case 24:
                    baseCost = 0.95f;
                    break;
                case 25:
                    baseCost = 1;
                    break;
                case 26:
                    baseCost = 1.03f;
                    break;
                case 27:
                    baseCost = 1.06f;
                    break;
                case 28:
                    baseCost = 1.11f;
                    break;
                case 29:
                    baseCost = 1.15f;
                    break;
                case 30:
                    baseCost = 1.2f;
                    break;
                default:
                    baseCost = 0;
            }
        } else if (shoeRarity == RARE) {
            switch (shoeLevel) {
                case 1:
                    baseCost = 0.51f;
                    break;
                case 2:
                    baseCost = 0.54f;
                    break;
                case 3:
                    baseCost = 0.57f;
                    break;
                case 4:
                    baseCost = 0.59f;
                    break;
                case 5:
                    baseCost = 0.61f;
                    break;
                case 6:
                    baseCost = 0.63f;
                    break;
                case 7:
                    baseCost = 0.65f;
                    break;
                case 8:
                    baseCost = 0.67f;
                    break;
                case 9:
                    baseCost = 0.69f;
                    break;
                case 10:
                    baseCost = 0.72f;
                    break;
                case 11:
                    baseCost = 0.75f;
                    break;
                case 12:
                    baseCost = 0.78f;
                    break;
                case 13:
                    baseCost = 0.81f;
                    break;
                case 14:
                    baseCost = 0.84f;
                    break;
                case 15:
                    baseCost = 0.87f;
                    break;
                case 16:
                    baseCost = 0.90f;
                    break;
                case 17:
                    baseCost = 0.94f;
                    break;
                case 18:
                    baseCost = 0.97f;
                    break;
                case 19:
                    baseCost = 1;
                    break;
                case 20:
                    baseCost = 1.04f;
                    break;
                case 21:
                    baseCost = 1.08f;
                    break;
                case 22:
                    baseCost = 1.12f;
                    break;
                case 23:
                    baseCost = 1.16f;
                    break;
                case 24:
                    baseCost = 1.2f;
                    break;
                case 25:
                    baseCost = 1.25f;
                    break;
                case 26:
                    baseCost = 1.3f;
                    break;
                case 27:
                    baseCost = 1.34f;
                    break;
                case 28:
                    baseCost = 1.39f;
                    break;
                case 29:
                    baseCost = 1.45f;
                    break;
                case 30:
                    baseCost = 1.5f;
                    break;
                default:
                    baseCost = 0;
            }
        } else if (shoeRarity == EPIC) {
            switch (shoeLevel) {
                case 1:
                    baseCost = 0.61f;
                    break;
                case 2:
                    baseCost = 0.65f;
                    break;
                case 3:
                    baseCost = 0.69f;
                    break;
                case 4:
                    baseCost = 0.72f;
                    break;
                case 5:
                    baseCost = 0.75f;
                    break;
                case 6:
                    baseCost = 0.78f;
                    break;
                case 7:
                    baseCost = 0.81f;
                    break;
                case 8:
                    baseCost = 0.84f;
                    break;
                case 9:
                    baseCost = 0.87f;
                    break;
                case 10:
                    baseCost = 0.91f;
                    break;
                case 11:
                    baseCost = 0.95f;
                    break;
                case 12:
                    baseCost = 0.99f;
                    break;
                case 13:
                    baseCost = 1.03f;
                    break;
                case 14:
                    baseCost = 1.07f;
                    break;
                case 15:
                    baseCost = 1.11f;
                    break;
                case 16:
                    baseCost = 1.15f;
                    break;
                case 17:
                    baseCost = 1.19f;
                    break;
                case 18:
                    baseCost = 1.24f;
                    break;
                case 19:
                    baseCost = 1.28f;
                    break;
                case 20:
                    baseCost = 1.33f;
                    break;
                case 21:
                    baseCost = 1.38f;
                    break;
                case 22:
                    baseCost = 1.43f;
                    break;
                case 23:
                    baseCost = 1.48f;
                    break;
                case 24:
                    baseCost = 1.51f;
                    break;
                case 25:
                    baseCost = 1.55f;
                    break;
                case 26:
                    baseCost = 1.59f;
                    break;
                case 27:
                    baseCost = 1.63f;
                    break;
                case 28:
                    baseCost = 1.66f;
                    break;
                case 29:
                    baseCost = 1.69f;
                    break;
                case 30:
                    baseCost = 1.72f;
                    break;
                default:
                    baseCost = 0;
            }
        } else {
            baseCost = 1;
        }
        return baseCost;
    }

    private void clearMbs() {
        ImageView[] boxImageViews = {mysteryBox1, mysteryBox2, mysteryBox3, mysteryBox4, mysteryBox5,
                mysteryBox6, mysteryBox7, mysteryBox8, mysteryBox9, mysteryBox10};
        TextView[] percentageTextViews = {mb1Percent, mb2Percent, mb3Percent, mb4Percent, mb5Percent,
                mb6Percent, mb7Percent, mb8Percent, mb9Percent, mb10Percent};
        for (int i = 0; i < 10; i++) {
            boxImageViews[i].setColorFilter(COLOR_GANDALF);
            boxImageViews[i].setAlpha(0.5f);
            percentageTextViews[i].setTextColor(COLOR_WHITE);
        }
    }

    // calculates mb chances
    private void calcMbChances() {
        final float totalLuck = Float.parseFloat(luckTotalTextView.getText().toString());
        final float localEnergy = (oneTwentyFive ? (float) (Math.round(oneTwentyFiveEnergy * 10) / 10.0) : energy);
        final String SIDEKICK_BASE_URL = "http://api.stepnsidekick.com/";

        if (localEnergy == 0 || totalLuck == 0) {
            return;
        }
        if (prevMbEnergy == localEnergy && prevMbLuck == totalLuck) {
            // energy & luck are the same as before, do not recalculate
            return;
        }

        clearMbs();

        if ((localEnergy * 10) % 2 != 0) {
            Toast.makeText(requireActivity(), "Energy should be a multiple of 0.2", Toast.LENGTH_SHORT).show();
            return;
        }

        mbLoadingSpinner.setVisibility(View.VISIBLE);
        prevMbLuck = totalLuck;

        if (prevMbEnergy != localEnergy) {
            // get updated energy/luck probs
            prevMbEnergy = localEnergy;
            Thread getMbChances = new Thread(new Runnable() {
                @Override
                public void run() {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(SIDEKICK_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    SidekickApi sidekickApi = retrofit.create(SidekickApi.class);
                    Call<MbChances> call = sidekickApi.getMbChances(localEnergy);
                    call.enqueue(new Callback<MbChances>() {
                        @Override
                        public void onResponse(@NonNull Call<MbChances> call, @NonNull Response<MbChances> response) {
                            MbChances jsonResponse = response.body();
                            if (jsonResponse != null) {
                                mbLuckIndices = jsonResponse.getLuck();
                                mbProbabilities = jsonResponse.getProbabilities();
                                findMbProbs(totalLuck, localEnergy);
                            } else {
                                updateMbs(new int[10]);
                                Toast.makeText(requireActivity(), "Could not fetch MB probabilities", Toast.LENGTH_SHORT).show();
                            }
                            mbLoadingSpinner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(@NonNull Call<MbChances> call, @NonNull Throwable t) {
                            // keep on truckin
                            mbLoadingSpinner.setVisibility(View.GONE);
                            Toast.makeText(requireActivity(), "Couldn't fetch MB probabilities", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            getMbChances.start();
            try {
                getMbChances.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // use the energy/luck probs we already have
            findMbProbs(totalLuck, localEnergy);
            mbLoadingSpinner.setVisibility(View.GONE);
        }
    }

    private void findMbProbs(float totalLuck, float localEnergy) {
        int gap = ((int) totalLuck - 1) % 10;
        int roundedLuck = (int) totalLuck - gap;
        if (gap > 5) {
            roundedLuck += 10;
        }
        if (roundedLuck > 11261) {
            roundedLuck = 11261;
        }
        try {
            updateMbs(mbProbabilities[mbLuckIndices[(roundedLuck - 1) / 10]]);
        } catch (NullPointerException e) {
            updateMbs(new int[10]);
        }
    }

    // update layout to display mb predictions
    private void updateMbs(int[] mbChances) {
        ImageView[] boxImageViews = {mysteryBox1, mysteryBox2, mysteryBox3, mysteryBox4, mysteryBox5,
                mysteryBox6, mysteryBox7, mysteryBox8, mysteryBox9, mysteryBox10};
        TextView[] percentageTextViews = {mb1Percent, mb2Percent, mb3Percent, mb4Percent, mb5Percent,
                mb6Percent, mb7Percent, mb8Percent, mb9Percent, mb10Percent};
        for (int i = 0; i < 10; i++) {
            String mbPercentage = mbChances[i] + "%";
            if (mbChances[i] >= 30) {
                boxImageViews[i].clearColorFilter();
                boxImageViews[i].setImageTintMode(null);
                boxImageViews[i].setAlpha(1.0f);
                percentageTextViews[i].setText(mbPercentage);
                percentageTextViews[i].setTextColor(COLOR_ALMOST_BLACK);
            } else if (mbChances[i] > 0) {
                boxImageViews[i].clearColorFilter();
                boxImageViews[i].setImageTintMode(null);
                boxImageViews[i].setAlpha(0.5f);
                percentageTextViews[i].setText(mbPercentage);
                percentageTextViews[i].setTextColor(COLOR_GANDALF);
            } else {
                boxImageViews[i].setColorFilter(COLOR_GANDALF);
                boxImageViews[i].setAlpha(0.5f);
                percentageTextViews[i].setTextColor(COLOR_WHITE);
            }
        }
    }


    private void updatePoints() {
        pointsAvailable = (shoeLevel * 2 * shoeRarity) - addedEff - addedLuck - addedComf - addedRes;

        if (pointsAvailable < 0) {
            pointsAvailable = (shoeLevel * 2 * shoeRarity);
            addedEff = 0;
            addedLuck = 0;
            addedComf = 0;
            addedRes = 0;
        }

        int gemsUnlocked = 0;
        gemEff = 0;
        gemLuck = 0;
        gemComf = 0;
        gemRes = 0;

        if (shoeLevel >= 20) {
            gemsUnlocked = 4;
        } else if (shoeLevel >= 15) {
            gemsUnlocked = 3;
        } else if (shoeLevel >= 10) {
            gemsUnlocked = 2;
        } else if (shoeLevel >= 5) {
            gemsUnlocked = 1;
        }

        for (int i = 0; i < gemsUnlocked; i++) {
            switch (gems.get(i).getSocketType()) {
                case EFF:
                    gems.get(i).setBasePoints(baseEff);
                    gemEff += gems.get(i).getTotalPoints();
                    break;
                case LUCK:
                    gems.get(i).setBasePoints(baseLuck);
                    gemLuck += gems.get(i).getTotalPoints();
                    break;
                case COMF:
                    gems.get(i).setBasePoints(baseComf);
                    gemComf += gems.get(i).getTotalPoints();
                    break;
                case RES:
                    gems.get(i).setBasePoints(baseRes);
                    gemRes += gems.get(i).getTotalPoints();
                    break;
                default:
            }
        }

        effTotalTextView.setText(String.valueOf(Math.floor((baseEff + addedEff + gemEff) * 10) / 10.0));
        luckTotalTextView.setText(String.valueOf(Math.floor((baseLuck + addedLuck + gemLuck) * 10) / 10.0));
        comfortTotalTextView.setText(String.valueOf(Math.floor((baseComf + addedComf + gemComf) * 10) / 10.0));
        resTotalTextView.setText(String.valueOf(Math.floor((baseRes + addedRes + gemRes) * 10) / 10.0));

        pointsAvailableTextView.setText(String.valueOf(pointsAvailable));

        if (pointsAvailable > 0) {
            effPlusTv.setTextColor(COLOR_ALMOST_BLACK);
            addEffButton.setClickable(true);
            luckPlusTv.setTextColor(COLOR_ALMOST_BLACK);
            addLuckButton.setClickable(true);
            comfPlusTv.setTextColor(COLOR_ALMOST_BLACK);
            addComfButton.setClickable(true);
            resPlusTv.setTextColor(COLOR_ALMOST_BLACK);
            addResButton.setClickable(true);
        } else {
            effPlusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            addEffButton.setClickable(false);
            luckPlusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            addLuckButton.setClickable(false);
            comfPlusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            addComfButton.setClickable(false);
            resPlusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            addResButton.setClickable(false);
        }

        if (addedEff > 0) {
            effMinusTv.setTextColor(COLOR_ALMOST_BLACK);
            subEffButton.setClickable(true);
        } else {
            effMinusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            subEffButton.setClickable(false);
        }

        if (addedLuck > 0) {
            luckMinusTv.setTextColor(COLOR_ALMOST_BLACK);
            subLuckButton.setClickable(true);
        } else {
            luckMinusTv.setTextColor(COLOR_GEM_SOCKET_SHADOW);
            subLuckButton.setClickable(false);
        }

        if (addedComf > 0) {
            comfMinusTv.setTextColor(COLOR_ALMOST_BLACK);
            subComfButton.setClickable(true);
        } else {
            comfMinusTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gem_socket_shadow));
            subComfButton.setClickable(false);
        }

        if (addedRes > 0) {
            resMinusTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.almost_black));
            subResButton.setClickable(true);
        } else {
            resMinusTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gem_socket_shadow));
            subResButton.setClickable(false);
        }

        pointsAvailableTextView.setText(String.valueOf(pointsAvailable));
        calcTotals();
    }

    // load initial point values
    private void loadPoints() {
        levelSeekbar.setProgress(shoeLevel - 1);
        if (oneTwentyFive) {
            energyEditText.setText(String.valueOf(oneTwentyFiveEnergy));
            oneTwentyFiveTextView.setText("125%");
        } else {
            energyEditText.setText(String.valueOf(energy));
            oneTwentyFiveTextView.setText("100%");
        }
        shoeNameEditText.setText(shoeName);
        updateHpRepairComfGem(comfGemHpRepairImageView);
        updateHpRepairComfGem(comfGemHpRepairTotalImageView);

        if (baseEff != 0) {
            effEditText.setText(String.valueOf(baseEff));
        } else {
            effEditText.setText("");
        }
        if (baseLuck != 0) {
            luckEditText.setText(String.valueOf(baseLuck));
        } else {
            luckEditText.setText("");
        }
        if (baseComf != 0) {
            comfortEditText.setText(String.valueOf(baseComf));
        } else {
            comfortEditText.setText("");
        }
        if (baseRes != 0) {
            resEditText.setText(String.valueOf(baseRes));
        } else {
            resEditText.setText("");
        }
    }

    // updates comf gem for HP repair
    private void updateHpRepairComfGem(ImageView gemView) {
        if (comfGemLvlForRepair == 2) {
            gemView.setImageResource(R.mipmap.gem_comf_level2);
            gemView.setPadding(0, 0, 0, 0);
        } else if (comfGemLvlForRepair == 3) {
            gemView.setImageResource(R.mipmap.gem_comf_level3);
            gemView.setPadding(0, 0, 0,0);
        } else {
            gemView.setImageResource(R.mipmap.gem_comf_level1);
            gemView.setPadding(0, (int) (4 * dpScale + 0.5f),0,0);
        }
    }

    // updates shoe nums
    private void updateShoeNums() {
        switch (shoeNum) {
            case 1:
                // labeled shoe 2
                shoeOneTextView.setText("1");
                shoeTwoTextView.setText("2");
                shoeThreeTextView.setText("3");
                break;
            case 2:
                // labeled shoe 3
                shoeOneTextView.setText("2");
                shoeTwoTextView.setText("3");
                shoeThreeTextView.setText("4");
                break;
            case 3:
                // labeled shoe 4
                shoeOneTextView.setText("3");
                shoeTwoTextView.setText("4");
                shoeThreeTextView.setText("5");
                break;
            case 4:
                // labeled shoe 5
                shoeOneTextView.setText("4");
                shoeTwoTextView.setText("5");
                shoeThreeTextView.setText("6");
                break;
            case 5:
                // labeled shoe 6
                shoeOneTextView.setText("5");
                shoeTwoTextView.setText("6");
                shoeThreeTextView.setText("");
                break;
            default:
                // labeled shoe 1
                shoeOneTextView.setText("");
                shoeTwoTextView.setText("1");
                shoeThreeTextView.setText("2");
                shoeNum = 0;
                break;
        }
    }

    // updates entire page with new shoe values
    private void updatePageNewShoe(final int oldShoeNum) {
        updateShoeNums();
        final String oldShoeNumString = (oldShoeNum == 0) ? "" : String.valueOf(oldShoeNum);
        final String shoeNumString = (shoeNum == 0) ? "" : String.valueOf(shoeNum);

        Thread saveEmLoadEm = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_ID, MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt(OPT_SHOE_TYPE_PREF + oldShoeNumString, shoeType);
                editor.putInt(SHOE_RARITY_PREF + oldShoeNumString, shoeRarity);
                editor.putInt(SHOE_LEVEL_PREF + oldShoeNumString, shoeLevel);
                editor.putFloat(OPT_ENERGY_PREF + oldShoeNumString, energy);
                editor.putFloat(ONE_TWENTY_FIVE_ENERGY_PREF + oldShoeNumString, oneTwentyFiveEnergy);
                editor.putFloat(BASE_EFF_PREF + oldShoeNumString, baseEff);
                editor.putInt(ADDED_EFF_PREF + oldShoeNumString, addedEff);
                editor.putFloat(BASE_LUCK_PREF + oldShoeNumString, baseLuck);
                editor.putInt(ADDED_LUCK_PREF + oldShoeNumString, addedLuck);
                editor.putFloat(BASE_COMF_PREF + oldShoeNumString, baseComf);
                editor.putInt(ADDED_COMF_PREF + oldShoeNumString, addedComf);
                editor.putFloat(BASE_RES_PREF + oldShoeNumString, baseRes);
                editor.putInt(ADDED_RES_PREF + oldShoeNumString, addedRes);
                editor.putString(SHOE_NAME + oldShoeNumString, shoeName);
                editor.putString(SHOE_IMAGE_URL + oldShoeNumString, shoeImageUrl);
                editor.putBoolean(ONE_TWENTY_FIVE_BOOL_PREF + oldShoeNumString, oneTwentyFive);
                editor.putBoolean(GMT_EARNING_PREF + oldShoeNumString, gmtEarningOn);
                editor.putInt(CHAIN_PREF + oldShoeNumString, shoeChain);
                editor.putBoolean(SHOE_LOCKED_PREF + oldShoeNumString, shoeLocked);

                editor.putInt(GEM_ONE_TYPE_PREF + oldShoeNumString, gems.get(0).getSocketType());
                editor.putInt(GEM_ONE_RARITY_PREF + oldShoeNumString, gems.get(0).getSocketRarity());
                editor.putInt(GEM_ONE_MOUNTED_PREF + oldShoeNumString, gems.get(0).getMountedGem());
                editor.putInt(GEM_TWO_TYPE_PREF + oldShoeNumString, gems.get(1).getSocketType());
                editor.putInt(GEM_TWO_RARITY_PREF + oldShoeNumString, gems.get(1).getSocketRarity());
                editor.putInt(GEM_TWO_MOUNTED_PREF + oldShoeNumString, gems.get(1).getMountedGem());
                editor.putInt(GEM_THREE_TYPE_PREF + oldShoeNumString, gems.get(2).getSocketType());
                editor.putInt(GEM_THREE_RARITY_PREF + oldShoeNumString, gems.get(2).getSocketRarity());
                editor.putInt(GEM_THREE_MOUNTED_PREF + oldShoeNumString, gems.get(2).getMountedGem());
                editor.putInt(GEM_FOUR_TYPE_PREF + oldShoeNumString, gems.get(3).getSocketType());
                editor.putInt(GEM_FOUR_RARITY_PREF + oldShoeNumString, gems.get(3).getSocketRarity());
                editor.putInt(GEM_FOUR_MOUNTED_PREF + oldShoeNumString, gems.get(3).getMountedGem());

                editor.apply();

                shoeType = sharedPreferences.getInt(OPT_SHOE_TYPE_PREF + shoeNumString, 0);
                shoeRarity = sharedPreferences.getInt(SHOE_RARITY_PREF + shoeNumString, COMMON);
                shoeLevel = sharedPreferences.getInt(SHOE_LEVEL_PREF + shoeNumString, 1);
                energy = sharedPreferences.getFloat(OPT_ENERGY_PREF + shoeNumString, 0);
                oneTwentyFiveEnergy = sharedPreferences.getFloat(ONE_TWENTY_FIVE_ENERGY_PREF + shoeNumString, 0);
                baseEff = sharedPreferences.getFloat(BASE_EFF_PREF + shoeNumString, 0);
                addedEff = sharedPreferences.getInt(ADDED_EFF_PREF + shoeNumString, 0);
                baseLuck = sharedPreferences.getFloat(BASE_LUCK_PREF + shoeNumString, 0);
                addedLuck = sharedPreferences.getInt(ADDED_LUCK_PREF + shoeNumString, 0);
                baseComf = sharedPreferences.getFloat(BASE_COMF_PREF + shoeNumString, 0);
                addedComf = sharedPreferences.getInt(ADDED_COMF_PREF + shoeNumString, 0);
                baseRes = sharedPreferences.getFloat(BASE_RES_PREF + shoeNumString, 0);
                addedRes = sharedPreferences.getInt(ADDED_RES_PREF + shoeNumString, 0);
                shoeName = sharedPreferences.getString(SHOE_NAME + shoeNumString, "");
                shoeImageUrl = sharedPreferences.getString(SHOE_IMAGE_URL + shoeNumString, "");
                oneTwentyFive = sharedPreferences.getBoolean(ONE_TWENTY_FIVE_BOOL_PREF + shoeNumString, false);
                gmtEarningOn = sharedPreferences.getBoolean(GMT_EARNING_PREF + shoeNumString, false);
                shoeChain = sharedPreferences.getInt(CHAIN_PREF + shoeNumString, SOL);
                shoeLocked = sharedPreferences.getBoolean(SHOE_LOCKED_PREF + shoeNumString, false);

                gems.clear();

                gems.add(new Gem(
                        sharedPreferences.getInt(GEM_ONE_TYPE_PREF + shoeNumString, -1),
                        sharedPreferences.getInt(GEM_ONE_RARITY_PREF + shoeNumString, 0),
                        sharedPreferences.getInt(GEM_ONE_MOUNTED_PREF + shoeNumString, 0)));
                gems.add(new Gem(
                        sharedPreferences.getInt(GEM_TWO_TYPE_PREF + shoeNumString, -1),
                        sharedPreferences.getInt(GEM_TWO_RARITY_PREF + shoeNumString, 0),
                        sharedPreferences.getInt(GEM_TWO_MOUNTED_PREF + shoeNumString, 0)));
                gems.add(new Gem(
                        sharedPreferences.getInt(GEM_THREE_TYPE_PREF + shoeNumString, -1),
                        sharedPreferences.getInt(GEM_THREE_RARITY_PREF + shoeNumString, 0),
                        sharedPreferences.getInt(GEM_THREE_MOUNTED_PREF + shoeNumString, 0)));
                gems.add(new Gem(
                        sharedPreferences.getInt(GEM_FOUR_TYPE_PREF + shoeNumString, -1),
                        sharedPreferences.getInt(GEM_FOUR_RARITY_PREF + shoeNumString, 0),
                        sharedPreferences.getInt(GEM_FOUR_MOUNTED_PREF + shoeNumString, 0)));
            }
        });
        saveEmLoadEm.start();
        try {
            saveEmLoadEm.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateGmtSwitch();
        loadPoints();
        updateType();
        updateRarity();
        updateChain();
        updateShoeLockStatus();
        calcTotals();
    }

    private void updateChain() {
        switch (shoeChain) {
            case BSC:
                chainSelectButton.setImageResource(R.drawable.button_bsc);
                chainSelectTv.setText("BSC");
                break;
            case ETH:
                chainSelectButton.setImageResource(R.drawable.button_eth);
                chainSelectTv.setText("ETH");
                break;
            default:
                chainSelectButton.setImageResource(R.drawable.button_sol);
                chainSelectTv.setText("SOL");
                break;
        }
    }

    // updates GMT switch for new shoe
    private void updateGmtSwitch() {
        coinSelectButton.setImageResource(gmtEarningOn ? R.drawable.button_gmt : R.drawable.button_gst);
        coinSelectTv.setText(gmtEarningOn ? "GMT" : "GST");
        coinSelectIcon.setImageResource(gmtEarningOn ? R.drawable.logo_gmt : R.drawable.logo_gst);

        estGstGmtLabelTv.setText(gmtEarningOn ? getString(R.string.est_gmt_range) : getString(R.string.est_gst_daily_limit));
        gstLimitPerDaySlashTextView.setVisibility(gmtEarningOn ? View.GONE : View.VISIBLE);
        gstLimitTextView.setVisibility(gmtEarningOn ? View.GONE : View.VISIBLE);
        estGstGmtIcon.setImageResource(gmtEarningOn ? R.drawable.coin_gmt : R.drawable.coin_gst);

        totalGmtIcon.setVisibility(gmtEarningOn ? View.VISIBLE : View.GONE);
        gmtTotalMinusTv.setVisibility(gmtEarningOn ? View.VISIBLE : View.GONE);
        gmtTotalTv.setVisibility(gmtEarningOn ? View.VISIBLE : View.GONE);

        optimizeGstGmtTextView.setText(gmtEarningOn ? getString(R.string.optimize_gmt) : getString(R.string.optimize_gst));
        optimizeGstGmtTextViewShadow.setText(gmtEarningOn ? getString(R.string.optimize_gmt) : getString(R.string.optimize_gst));

        changeGmtEstimateButton.setVisibility(gmtEarningOn ? View.VISIBLE : View.GONE);
        changeGstLimitButton.setVisibility(gmtEarningOn ? View.GONE : View.VISIBLE);
        toggleGstEstLimitButton.setVisibility(gmtEarningOn ? View.GONE : View.VISIBLE);
        useGstLimit = false;
    }

    // clears focus from the input boxes by focusing on another hidden edittext
    private void clearFocus(View view) {
        shoeNameEditText.clearFocus();
        energyEditText.clearFocus();
        effEditText.clearFocus();
        luckEditText.clearFocus();
        comfortEditText.clearFocus();
        resEditText.clearFocus();
        comfGemPriceEditText.clearFocus();

        focusThief.requestFocus();

        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // resets all values on page
    private void resetPage() {
        shoeType = WALKER;
        shoeRarity = COMMON;
        shoeLevel = 1;
        energy = 0;
        oneTwentyFiveEnergy = 0;
        oneTwentyFive = false;
        baseEff = 0;
        addedEff = 0;
        baseLuck = 0;
        addedLuck = 0;
        baseComf = 0;
        addedComf = 0;
        baseRes = 0;
        addedRes = 0;
        shoeName = "";
        gemLuck = 0;
        gemComf = 0;
        gemRes = 0;

        gems.clear();

        gems.add(new Gem(-1, 0,0));
        gems.add(new Gem(-1, 0,0));
        gems.add(new Gem(-1, 0,0));
        gems.add(new Gem(-1, 0,0));

        updateType();
        updateRarity();
        loadPoints();

        hpLossTextView.setText("0");
        repairCostHpTextView.setText("0");
        gemMultipleTextView.setText("0");
        gemMultipleTotalTextView.setText("- 0");
        estGstGmtTextView.setText("0");
        durabilityLossTextView.setText("0");
        repairCostDurTextView.setText("0");
        totalIncomeTextView.setText("0");

        Toast.makeText(requireActivity(), "Values Reset", Toast.LENGTH_SHORT).show();
    }

    private void getGemPrices() {
        /*
        BASE URL:   "https://apilb.stepn.com/"
        add:        "run/orderlist?saleId=1&order=2001&chain={chainNum}&refresh=true&page=0&otd=&type=501&gType=3&quality=&level={gemLevel}&bread=0"

        chainNums:  SOL: 103
                    BSC: 104
                    ETH: 101

        gemLevels:  1:  2010
                    2:  3010
                    3:  4010

        EXAMPLE: BSC level 2 gem price: https://apilb.stepn.com/run/orderlist?saleId=1&order=2001&chain=104&refresh=true&page=0&otd=&type=501&gType=3&quality=&level=3010&bread=0
         */

        Thread fetchGemPrices = new Thread(new Runnable() {
            @Override
            public void run() {

                final String GEM_BASE_URL = "https://apilb.stepn.com/";
                int chainCode;

                switch (shoeChain) {
                    case BSC:
                        chainCode = 104;
                        break;
                    case ETH:
                        chainCode = 101;
                        break;
                    default:
                        chainCode = 103;
                }


                Retrofit retrofitGems = new Retrofit.Builder().baseUrl(GEM_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                GemApi myGemApi = retrofitGems.create(GemApi.class);

                Call<GemPrices> callGems = myGemApi.getLevelOne(chainCode);

                callGems.enqueue(new Callback<GemPrices>() {
                    @Override
                    public void onResponse(Call<GemPrices> call, Response<GemPrices> response) {

                        try {
                            GemPrices priceList = response.body();

                            GEM_PRICES[0] = (double) priceList.getPrice() / 100.0;

                            if (comfGemLvlForRepair == 1 && fragActive) {
                                comfGemPrice = GEM_PRICES[0];
                                comfGemPriceEditText.setText(String.valueOf(comfGemPrice));
                                calcTotals();
                            }

                        } catch (NullPointerException e) {
                            if (fragActive) {
                                Toast.makeText(requireActivity(), "Unable to get gem prices. Try again in a few moments", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GemPrices> call, Throwable t) {
                        if (fragActive) {
                            Toast.makeText(requireActivity(), "Unable to get gem prices. Try again in a few moments", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                callGems = myGemApi.getLevelTwo(chainCode);

                callGems.enqueue(new Callback<GemPrices>() {
                    @Override
                    public void onResponse(Call<GemPrices> call, Response<GemPrices> response) {

                        try {
                            GemPrices priceList = response.body();

                            GEM_PRICES[1] = (double) priceList.getPrice() / 100.0;
                            if (comfGemLvlForRepair == 2 && fragActive) {
                                comfGemPrice = GEM_PRICES[1];
                                comfGemPriceEditText.setText(String.valueOf(comfGemPrice));
                                calcTotals();
                            }

                        } catch (NullPointerException ignored) {
                        }
                    }

                    @Override
                    public void onFailure(Call<GemPrices> call, Throwable t) {
                    }
                });

                callGems = myGemApi.getLevelThree(chainCode);

                callGems.enqueue(new Callback<GemPrices>() {
                    @Override
                    public void onResponse(Call<GemPrices> call, Response<GemPrices> response) {

                        try {
                            GemPrices priceList = response.body();

                            GEM_PRICES[2] = (double) priceList.getPrice() / 100.0;
                            if (comfGemLvlForRepair == 3 && fragActive) {
                                comfGemPrice = GEM_PRICES[2];
                                comfGemPriceEditText.setText(String.valueOf(comfGemPrice));
                                calcTotals();
                            }

                        } catch (NullPointerException ignored) {
                        }
                    }

                    @Override
                    public void onFailure(Call<GemPrices> call, Throwable t) {
                    }
                });
            }
        });
        fetchGemPrices.start();
        try {
            fetchGemPrices.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // to save prefs
    @Override
    public void onStop() {
        fragActive = false;
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        final String shoeNumString = (shoeNum == 0) ? "" : String.valueOf(shoeNum);

        editor.putInt(OPT_SHOE_TYPE_PREF + shoeNumString, shoeType);
        editor.putFloat(OPT_ENERGY_PREF + shoeNumString, energy);
        editor.putFloat(ONE_TWENTY_FIVE_ENERGY_PREF + shoeNumString, oneTwentyFiveEnergy);
        editor.putInt(SHOE_RARITY_PREF + shoeNumString, shoeRarity);
        editor.putInt(SHOE_LEVEL_PREF + shoeNumString, shoeLevel);
        editor.putFloat(BASE_EFF_PREF + shoeNumString, baseEff);
        editor.putInt(ADDED_EFF_PREF + shoeNumString, addedEff);
        editor.putFloat(BASE_LUCK_PREF + shoeNumString, baseLuck);
        editor.putInt(ADDED_LUCK_PREF + shoeNumString, addedLuck);
        editor.putFloat(BASE_COMF_PREF + shoeNumString, baseComf);
        editor.putInt(ADDED_COMF_PREF + shoeNumString, addedComf);
        editor.putFloat(BASE_RES_PREF + shoeNumString, baseRes);
        editor.putInt(ADDED_RES_PREF + shoeNumString, addedRes);
        editor.putInt(COMF_GEM_HP_REPAIR, comfGemLvlForRepair);
        editor.putBoolean(UPDATE_PREF, false);
        editor.putString(SHOE_NAME + shoeNumString, shoeName);
        editor.putString(SHOE_IMAGE_URL + shoeNumString, shoeImageUrl);
        editor.putBoolean(ONE_TWENTY_FIVE_BOOL_PREF + shoeNumString, oneTwentyFive);
        editor.putBoolean(GMT_EARNING_PREF + shoeNumString, gmtEarningOn);
        editor.putInt(CHAIN_PREF + shoeNumString, shoeChain);
        editor.putInt(SHOE_NUM_PREF, shoeNum);
        editor.putInt(GMT_PERCENT_MODIFIER_PREF, gmtPercentModifier);
        editor.putBoolean(SHOE_LOCKED_PREF + shoeNumString, shoeLocked);

        editor.putInt(GEM_ONE_TYPE_PREF + shoeNumString, gems.get(0).getSocketType());
        editor.putInt(GEM_ONE_RARITY_PREF + shoeNumString, gems.get(0).getSocketRarity());
        editor.putInt(GEM_ONE_MOUNTED_PREF + shoeNumString, gems.get(0).getMountedGem());
        editor.putInt(GEM_TWO_TYPE_PREF + shoeNumString, gems.get(1).getSocketType());
        editor.putInt(GEM_TWO_RARITY_PREF + shoeNumString, gems.get(1).getSocketRarity());
        editor.putInt(GEM_TWO_MOUNTED_PREF + shoeNumString, gems.get(1).getMountedGem());
        editor.putInt(GEM_THREE_TYPE_PREF + shoeNumString, gems.get(2).getSocketType());
        editor.putInt(GEM_THREE_RARITY_PREF + shoeNumString, gems.get(2).getSocketRarity());
        editor.putInt(GEM_THREE_MOUNTED_PREF + shoeNumString, gems.get(2).getMountedGem());
        editor.putInt(GEM_FOUR_TYPE_PREF + shoeNumString, gems.get(3).getSocketType());
        editor.putInt(GEM_FOUR_RARITY_PREF + shoeNumString, gems.get(3).getSocketRarity());
        editor.putInt(GEM_FOUR_MOUNTED_PREF + shoeNumString, gems.get(3).getMountedGem());

        editor.apply();

        super.onStop();
    }
}