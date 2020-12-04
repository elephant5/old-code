package com.colourfulchina.pangu.taishang.event;

        import lombok.Data;
        import org.springframework.context.ApplicationEvent;

@Data
public class SysOperateLogEvent extends ApplicationEvent{
    private static final long serialVersionUID = -7736755286342186007L;
    public SysOperateLogEvent(Object source) {
        super(source);
    }

}
