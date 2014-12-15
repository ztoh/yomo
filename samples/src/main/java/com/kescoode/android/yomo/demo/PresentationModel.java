package com.kescoode.android.yomo.demo;

import android.util.Log;

import com.kescoode.android.yomo.toolbox.TaskSet;
import com.kescoode.android.yomo.toolbox.Yomo;

import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

@org.robobinding.annotation.PresentationModel
public class PresentationModel implements HasPresentationModelChangeSupport {
    private PresentationModelChangeSupport changeSupport;
    private static long index = 0L;
    private long result = 0L;
    private String name = "";

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
            public Object background() {
                result = 0L;
                index++;
                for (int i = 0; i < index * 1000; i++) {
                    result += 2* i;
                }
                return super.background();
            }
        };
        task.done(new TaskSet.UiDoneCallBack() {
            @Override
            public void done(Object result) {
                if (null == result) {
                    Log.e("hahah", "nimabe");
                }
                changeSupport.firePropertyChange("result");
            }
        });

        TaskSet next = new TaskSet() {
            @Override
            public Object background() {
                for (int i = 0; i < 1000; i++) {
                    Log.e("nimabe", "test");
                }
                return super.background();
            }
        };

        next.done(new TaskSet.UiDoneCallBack() {
            @Override
            public void done(Object args) {
                result = 0L;
                changeSupport.firePropertyChange("result");
            }
        });
        next.delay(10000L);
        task.next(next);
        Yomo.concurrence(task);
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return changeSupport;
    }
}
