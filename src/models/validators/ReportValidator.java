package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Report;

public class ReportValidator {
    public static List<String> validate(Report r) {
        List<String> errors = new ArrayList<String>();

        String title_error = _validateTitle(r.getTitle());
        if (!title_error.equals("")) {
            errors.add(title_error);
        }

        String content_error = _validateContent(r.getContent());
        if (!content_error.equals("")) {
            errors.add(content_error);
        }
        //
        String publish_error = _validateRange(r.getPublish());
        if (!publish_error.equals("")) {
            errors.add(publish_error);
        }
        //
        return errors;
    }

    private static String _validateTitle(String title) {
        if (title == null || title.equals("")) {
            return "タイトルを入力してください。";
        }

        return "";
    }

    private static String _validateContent(String content) {
        if (content == null || content.equals("")) {
            return "内容を入力してください。";
        }

        return "";
    }

    //
    private static String _validateRange(Integer publish) {
        if (publish == 0 || publish.equals("")) {
            /* option value = 0 */
            return "公開範囲を設定してください。";
        }

        return "";
    }

}