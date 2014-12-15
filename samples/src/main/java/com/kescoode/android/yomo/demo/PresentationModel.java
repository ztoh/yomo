package com.kescoode.android.yomo.demo;

import android.util.Log;

import com.kescoode.android.yomo.TaskSet;
import com.kescoode.android.yomo.Yomo;
import com.kescoode.android.yomo.YomoError;

import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

@org.robobinding.annotation.PresentationModel
public class PresentationModel implements HasPresentationModelChangeSupport {
    private PresentationModelChangeSupport changeSupport;
    private static long index = 0L;
    private long result = 0L;

    public PresentationModel() {
        changeSupport = new PresentationModelChangeSupport(this);
    }

    public String getResult() {
        return "Result: " + result;
    }

    public String getRun() {
        return "运行";
    }

    public void runTask() {
        TaskSet task = new TaskSet() {
            @Override
            public Object background() throws Exception {
                result = 0L;
                index++;
                for (int i = 0; i < index * 1000; i++) {
                    result += 2 * i;
                }
                return super.background();
            }
        };
        task.done(new TaskSet.UiDoneCallBack() {
            @Override
            public void done(Object result) {
                if (null == result) {
                    Log.e("hahah", "test");
                }
                changeSupport.firePropertyChange("result");
            }
        });

        TaskSet<Integer> next = new TaskSet<Integer>() {
            @Override
            public Integer background() throws Exception {
//                throwFailed(new Exception("test"));
                return 1000;
            }
        };
        next.done(new TaskSet.UiDoneCallBack<Integer>() {
            @Override
            public void done(Integer args) {
                result = args;
                changeSupport.firePropertyChange("result");
            }
        });
        next.fail(new TaskSet.UiFailCallBack() {
            @Override
            public void fail(YomoError error) {
                error.getCauses()[0].printStackTrace();
            }
        });
        next.delay(10000L);
        task.next(next);
        Yomo.add(task, this);
//        task.cancel();
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return changeSupport;
    }
}
