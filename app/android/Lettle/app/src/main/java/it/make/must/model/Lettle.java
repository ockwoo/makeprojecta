package it.make.must.model;

import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by EVOL on 2016-07-04.
 */
public class Lettle implements Serializable {

    private String id;
    private String updatedAt;
    private String senderId;
    private String creator;
    private String modifier;
    private String datetime;
    private Event event;
    private Contents contents;
    private String[] receiversId;

    public Lettle(String receiver, String title, String msg, double lat, double lon) {
        this.creator = this.modifier =  this.senderId = "clientMan";

        this.contents = new Contents();
        this.contents.setTitle(title);
        this.contents.setMsg(msg);

        this.event = new Event();

        //this.event.setLatitude((long)37.186581);
        //this.event.setLongitude((long)127.071928);

        this.event.setLatitude(lat);
        this.event.setLongitude(lon);

        this.receiversId = receiver.split(",");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String sdt = dateFormat.format(new Date(System.currentTimeMillis()));
        this.event.setTimestamp(sdt);
    }
    public Lettle(JSONObject jsonObject) {

        try{
            event = new Event();
            contents = new Contents();

            this.id = jsonObject.getString("_id");
            this.updatedAt = jsonObject.getString("updated_at");
            this.senderId = jsonObject.getString("senderId");
            this.creator = jsonObject.getString("creator");
            this.modifier = jsonObject.getString("modifier");

            // evevnt
            this.event.setLatitude(jsonObject.getJSONObject("event").getDouble("latitude"));
            this.event.setLongitude(jsonObject.getJSONObject("event").getDouble("longitude"));
            this.event.setTimestamp(jsonObject.getJSONObject("event").getString("timestamp"));

            // contents
            this.contents.setTitle(jsonObject.getJSONObject("contents").getString("title"));
            this.contents.setMsg(jsonObject.getJSONObject("contents").getString("msg"));

            JSONArray array = jsonObject.optJSONArray("receiversId");
            int size = array.length();
            this.receiversId = new String[size];
            for (int i = 0; i < array.length(); ++i) {
                this.receiversId[i] = array.optString(i);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date tempDate = sdf.parse(jsonObject.getJSONObject("event").getString("timestamp"));
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            this.datetime = sdf2.format(tempDate);

        } catch (Exception e) {
            Log.d("Lettle","JSONException - " + e.toString());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdated_at(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }

    public String[] getReceiversId() {
        return receiversId;
    }

    public String getDateTime() {
        return datetime;
    }

    public void setDateTime(String datetime) {
        this.datetime = datetime;
    }

    public String getReceiversId(int idx) {
        if(receiversId != null && receiversId.length > idx)
            return receiversId[idx];

        return null;
    }

    public void setReceiversId(String[] receiversId) {
        this.receiversId = receiversId;
    }

    public String toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("senderId", this.senderId);

            JSONArray arrReceiversId = new JSONArray();
            for(int i = 0; i < this.receiversId.length; i++) {
                arrReceiversId.put(receiversId[i]);
            }
            jsonObject.put("receiversId", arrReceiversId);

            JSONObject jsonContents = new JSONObject();
            jsonContents.put("title", this.contents.getTitle());
            jsonContents.put("msg", this.contents.getMsg());
            jsonObject.put("contents", jsonContents);

            JSONObject jsonEvent= new JSONObject();
            jsonEvent.put("latitude", this.event.getLatitude());
            jsonEvent.put("longitude", this.event.getLongitude());
            jsonEvent.put("timestamp", this.event.getTimestamp());
            jsonObject.put("event", jsonEvent);

            jsonObject.put("creator", this.creator);
            jsonObject.put("modifier", this.modifier);

            return jsonObject.toString();

        } catch (JSONException e) {
            Log.d("Lettle","JSONException - " + e.toString());
        }
        return null;
    }
}
