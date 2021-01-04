package de.fll.regiom.model.evaluation;

public final class EvaluationEntry {

    private final String title;
    private final String[] options;
    private int checked = -1;

    public EvaluationEntry(String title, String... options) {
        this.title = title;
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public String[] getOptions() {
        return options;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        if (checked < 0 || checked > 3)
            return;
        this.checked = checked;
    }
}
