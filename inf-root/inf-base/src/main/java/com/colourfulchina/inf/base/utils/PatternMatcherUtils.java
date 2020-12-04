package com.colourfulchina.inf.base.utils;

import com.colourfulchina.inf.base.dto.MatcherGroupDto;
import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class PatternMatcherUtils {
    public static MatcherGroupDto getMatcher(String regex, String source) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            MatcherGroupDto groupDto=new MatcherGroupDto();
            groupDto.setGroup0(matcher.group(0));
            groupDto.setGroup1(matcher.group(1));
            groupDto.setGroup2(matcher.group(2));
            return groupDto;
        }
        return null;
    }
}
