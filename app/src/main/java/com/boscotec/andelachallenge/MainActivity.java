package com.boscotec.andelachallenge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boscotec.andelachallenge.adapter.UserAdapter;
import com.boscotec.andelachallenge.model.User;
import com.boscotec.andelachallenge.model.UserDetail;
import com.boscotec.andelachallenge.rest.ApiClient;
import com.boscotec.andelachallenge.rest.ApiInterface;
import com.boscotec.andelachallenge.utility.ConnectivityReceiver;
import com.boscotec.andelachallenge.utility.NewSimpleDividerItemDecoration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {

    Toolbar toolbar, searchtoolbar;
    Menu search_menu;
    MenuItem item_search;

    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setHasFixedSize(true);

        getUser();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected && findViewById(R.id.emptyTextView).getVisibility()==View.VISIBLE ){
            getUser();
            findViewById(R.id.emptyTextView).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }


    private void getUser(){

        progressbar.setVisibility(VISIBLE);
        recyclerView.setVisibility(View.GONE);

        if(!ConnectivityReceiver.isConnected()){
            findViewById(R.id.emptyTextView).setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        final String filter = "language:java location:lagos";
        ApiInterface connectToApi = ApiClient.getClient().create(ApiInterface.class);
        Call<User> call = connectToApi.getUser(filter);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    List<UserDetail> items = new ArrayList();

                    for(int i = 0; i < response.body().getItems().size(); i++){
                        items.add(new UserDetail(
                                response.body().getItems().get(i).getLogin(),
                                response.body().getItems().get(i).getAvatarUrl(),
                                response.body().getItems().get(i).getUrl(),
                                response.body().getItems().get(i).getHtmlUrl()));
                    }

                    adapter = new UserAdapter(getBaseContext(),items);
                    recyclerView.addItemDecoration(new NewSimpleDividerItemDecoration(getBaseContext()));
                    recyclerView.setAdapter(adapter);


                    if(adapter.getItemCount()==0) {
                        findViewById(R.id.emptyTextView).setVisibility(View.VISIBLE);
                    }else{
                        toolbar.getMenu().findItem(R.id.action_search).setVisible(true);
                    }

                    progressbar.setVisibility(View.GONE);
                    recyclerView.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                findViewById(R.id.emptyTextView).setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                Log.d("onFailure", t.toString());
            }
        });

    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbarTitle);
        title.setText(getResources().getString(R.string.mainactivity_toolbar_title));
        setSupportActionBar(toolbar);
        setSearchtoolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            case R.id.action_search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.toolbar_search,1,true,true);
                else
                    searchtoolbar.setVisibility(VISIBLE);

                item_search.expandActionView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setSearchtoolbar()
    {
        searchtoolbar = (Toolbar) findViewById(R.id.toolbar_search);
        if ( searchtoolbar != null) {
            searchtoolbar.inflateMenu(R.menu.menu_search);
            search_menu= searchtoolbar.getMenu();

            searchtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.toolbar_search,1,true,false);
                    else
                        searchtoolbar.setVisibility(View.GONE);
                }
            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.toolbar_search,1,true,false);
                    }
                    else
                        searchtoolbar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });
            initSearchView();
        } else
            Log.d("toolbar", "setSearchtollbar: NULL");
    }

    public void initSearchView()
    {
        final SearchView searchView =
                (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        // Enable/Disable Submit button in the keyboard

        searchView.setSubmitButtonEnabled(false);

        // Change search close button image

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close);


        // set hint and the text colors

        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));


        // set the cursor

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                if(adapter != null){
                    adapter.getFilter().filter(query);
                    Log.i("query", "" + query);
                }
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow)
    {
        final View myView = findViewById(viewID);

        int width=myView.getWidth();

        if(posFromRight>0)
            width-=(posFromRight*getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material))-(getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)/ 2);
        if(containsOverflow)
            width-=getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx=width;
        int cy=myView.getHeight()/2;

        Animator anim;
        if(isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0,(float)width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float)width, 0);

        anim.setDuration((long)220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(!isShow)
                {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if(isShow)
            myView.setVisibility(VISIBLE);

        // start the animation
        anim.start();
    }

}
