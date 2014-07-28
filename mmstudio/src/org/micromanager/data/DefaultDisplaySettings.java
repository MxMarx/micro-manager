package org.micromanager.data;

import java.awt.Color;

import org.json.JSONException;
import org.json.JSONObject;

import org.micromanager.api.data.DisplaySettings;
import org.micromanager.api.MultiStagePosition;

import org.micromanager.utils.ReportingUtils;

public class DefaultDisplaySettings implements DisplaySettings {

   public static class Builder implements DisplaySettings.DisplaySettingsBuilder {
      private String[] channelNames_ = null;
      private Color[] channelColors_ = null;
      private Integer[] channelContrastMins_ = null;
      private Integer[] channelContrastMaxes_ = null;

      @Override
      public DefaultDisplaySettings build() {
         return new DefaultDisplaySettings(this);
      }
      
      @Override
      public DisplaySettingsBuilder channelNames(String[] channelNames) {
         channelNames_ = channelNames;
         return this;
      }

      @Override
      public DisplaySettingsBuilder channelColors(Color[] channelColors) {
         channelColors_ = channelColors;
         return this;
      }

      @Override
      public DisplaySettingsBuilder channelContrastMins(Integer[] channelContrastMins) {
         channelContrastMins_ = channelContrastMins;
         return this;
      }

      @Override
      public DisplaySettingsBuilder channelContrastMaxes(Integer[] channelContrastMaxes) {
         channelContrastMaxes_ = channelContrastMaxes;
         return this;
      }
   }

   private String[] channelNames_ = null;
   private Color[] channelColors_ = null;
   private Integer[] channelContrastMins_ = null;
   private Integer[] channelContrastMaxes_ = null;

   public DefaultDisplaySettings(Builder builder) {
      channelNames_ = builder.channelNames_;
      channelColors_ = builder.channelColors_;
      channelContrastMins_ = builder.channelContrastMins_;
      channelContrastMaxes_ = builder.channelContrastMaxes_;
   }

   @Override
   public String[] getChannelNames() {
      return channelNames_;
   }

   @Override
   public Color[] getChannelColors() {
      return channelColors_;
   }

   @Override
   public Integer[] getChannelContrastMins() {
      return channelContrastMins_;
   }

   @Override
   public Integer[] getChannelContrastMaxes() {
      return channelContrastMaxes_;
   }
   
   @Override
   public DisplaySettingsBuilder copy() {
      return new DefaultMetadataBuilder()
            .channelNames(channelNames_)
            .channelColors(channelColors_)
            .channelContrastMins(channelContrastMins_)
            .channelContrastMaxes(channelContrastMaxes_);
   }

   /**
    * For backwards compatibility, generate a DefaultDisplaySettings from
    * a JSONObject.
    */
   public static DefaultDisplaySettings legacyFromJSON(JSONObject tags) {
      return new Builder()
         .channelNames(new String[] {MDUtils.getChannelName(tags)})
         .channelColors(new Color[] {MDUtils.getChannelColor(tags)})
         .channelContrastMins(new Integer[] {tags.get("ChContrastMin")})
         .channelContrastMaxes(new Integer[] {tags.get("ChContrastMaxes")})
         .build();
   }

   /**
    * For backwards compatibility, generate a JSONObject representing this
    * DefaultDisplaySettings.
    */
   @Override
   public JSONObject legacyToJSON() {
      try {
         result = new JSONObject();
         MDUtils.setChannelName(channelNames_[0]);
         // TODO: no idea how we represent a color with an int in the current
         // system, but at least using a hashCode() uniquely represents this
         // RGBA color!
         MDUtils.setChannelColor(channelColors_[0].hashCode());
         result.put("ChContrastMin", channelContrastMins_[0]);
         result.put("ChContrastMax", channelContrastMaxes_[0]);
         return result;
      }
      catch (JSONException e) {
         ReportingUtils.logError(e, "Couldn't convert DefaultDisplaySettings to JSON");
         return null;
      }
   }
}