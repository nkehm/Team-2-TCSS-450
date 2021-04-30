package ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import edu.uw.tcss450.nkehm.R;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class FirstFragmentDirections {
  private FirstFragmentDirections() {
  }

  @NonNull
  public static ActionFirstFragmentToColorFragment actionFirstFragmentToColorFragment(int color) {
    return new ActionFirstFragmentToColorFragment(color);
  }

  public static class ActionFirstFragmentToColorFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionFirstFragmentToColorFragment(int color) {
      this.arguments.put("color", color);
    }

    @NonNull
    public ActionFirstFragmentToColorFragment setColor(int color) {
      this.arguments.put("color", color);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("color")) {
        int color = (int) arguments.get("color");
        __result.putInt("color", color);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_firstFragment_to_colorFragment;
    }

    @SuppressWarnings("unchecked")
    public int getColor() {
      return (int) arguments.get("color");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionFirstFragmentToColorFragment that = (ActionFirstFragmentToColorFragment) object;
      if (arguments.containsKey("color") != that.arguments.containsKey("color")) {
        return false;
      }
      if (getColor() != that.getColor()) {
        return false;
      }
      if (getActionId() != that.getActionId()) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + getColor();
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionFirstFragmentToColorFragment(actionId=" + getActionId() + "){"
          + "color=" + getColor()
          + "}";
    }
  }
}
