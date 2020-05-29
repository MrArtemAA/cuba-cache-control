package ru.artemaa.cuba.cachecontrol.web.screens;

import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import com.haulmont.cuba.gui.screen.*;
import ru.artemaa.cache.CacheInfo;
import ru.artemaa.cuba.cachecontrol.service.CacheControlService;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@UiController("cachectrl_CacheControlPanel")
@UiDescriptor("cache-control-panel.xml")
public class CacheControlPanel extends Screen {
    @Inject
    private CacheControlService cacheControlService;

    @Inject
    private BackgroundWorker backgroundWorker;

    @Inject
    private Notifications notifications;
    @Inject
    private Dialogs dialogs;

    @Inject
    private ProgressBar progressBar;
    @Inject
    private Form form;
    @Inject
    private LookupField<String> cacheNameField;
    @Inject
    private DateField<LocalDateTime> lastSyncField;
    @Inject
    private TextField<Long> totalCachedItemsField;
    @Inject
    private Button refreshCacheBtn;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        Set<String> cacheNames = cacheControlService.cacheNames();
        cacheNameField.setOptionsList(new ArrayList<>(cacheNames));
    }

    @Subscribe("cacheNameField")
    public void onCacheNameFieldValueChange(HasValue.ValueChangeEvent event) {
        String cacheName = (String) event.getValue();
        if (cacheName != null) {
            updateCacheData(cacheName);
        }
    }

    @Subscribe("refreshCacheBtn")
    public void onRefreshCacheBtnClick(Button.ClickEvent event) {
        String cacheName = cacheNameField.getValue();
        if (cacheName != null) {
            BackgroundTask<Integer, Void> backgroundTask = new BackgroundTask<Integer, Void>(120L, this) {

                @Override
                public Void run(TaskLifeCycle<Integer> taskLifeCycle) throws Exception {
                    cacheControlService.refresh(cacheName);
                    return null;
                }

                @Override
                public void done(Void result) {
                    setSearchMode(false);
                    updateCacheData(cacheName);
                }

                @Override
                public boolean handleException(Exception ex) {
                    setSearchMode(false);
                    dialogs.createExceptionDialog()
                            .withThrowable(ex)
                            .show();
                    return true;
                }

                @Override
                public boolean handleTimeoutException() {
                    setSearchMode(false);
                    notifications.create(Notifications.NotificationType.ERROR)
                            .withDescription("Timeout")
                            .show();
                    return true;
                }
            };

            setSearchMode(true);
            backgroundWorker.handle(backgroundTask).execute();
        }
    }

    @Subscribe("closeBtn")
    public void onCloseBtnClick(Button.ClickEvent event) {
        close(StandardOutcome.CLOSE);
    }

    private void updateCacheData(String cacheName) {
        CacheInfo cacheInfo = cacheControlService.cacheInfo(cacheName);
        lastSyncField.setValue(cacheInfo.getLastSync());
        totalCachedItemsField.setValue(cacheInfo.getTotalCachedItems());
        refreshCacheBtn.setEnabled(true);
    }

    private void setSearchMode(boolean on) {
        form.setEditable(!on);
        refreshCacheBtn.setEnabled(!on);
        progressBar.setVisible(on);
    }
}