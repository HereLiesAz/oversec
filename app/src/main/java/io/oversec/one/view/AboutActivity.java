package io.oversec.one.view;

import android.os.Bundle;
import androidx.activity.compose.setContent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import io.oversec.one.iab.IabUtil;
import io.oversec.one.ui.screen.AboutScreen;
import io.oversec.one.ui.theme.OneTheme;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent {
            OneTheme {
                AboutScreen(
                    onBackPressed = () -> finish(),
                    isIabAvailable = IabUtil.getInstance(this).isIabAvailable()
                );
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
