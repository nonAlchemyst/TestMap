package com.example.testmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.example.testmap.viewmodel.Data;
import com.example.testmap.viewmodel.User;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MapAdapter {

    private final MapView _mapView;
    private List<DataMarker> overlays;
    private int userInFocusIndex = 0;
    private ItemizedIconOverlay<OverlayItem> meItem;

    public OnItemClick listener;

    public MapAdapter(MapView mapView){
        _mapView = mapView;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility", "SimpleDateFormat"})
    public void updateMap(List<Data> users){
//        _mapView.getOverlays().remove(usersItem);
//        ArrayList<OverlayItem> anotherOverlayItemArray = new ArrayList<>();
//        for(int i = 0; i < users.size(); i++){
//            Data user = users.get(i);
//            OverlayItem item = new DataOverlayItem(new GeoPoint(user.getLat(), user.getLon()), user);
//            item.setMarker(builtMarker(user.getImage()));
//            item.setMarkerHotspot(OverlayItem.HotspotPlace.CENTER);
//            anotherOverlayItemArray.add(item);
//        }
//        usersItem = new ItemizedIconOverlay<>(
//                _mapView.getContext(), anotherOverlayItemArray,
//                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
//            @Override
//            public boolean onItemSingleTapUp(int index, OverlayItem item) {
//                if(listener != null && item instanceof DataOverlayItem){
//                    listener.onClick(((DataOverlayItem)item).getData());
//                    return true;
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onItemLongPress(int index, OverlayItem item) {
//                return false;
//            }
//        });
//        _mapView.getOverlays().add(usersItem);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        _mapView.getOverlays().remove(overlays);
        overlays = new ArrayList<>();
        for (int i = 0; i < users.size(); i++){
            Data user = users.get(i);
            DataMarker marker = new DataMarker(_mapView, user);
            marker.setIcon(builtMarker(user.getImage()));
            MyInfoWindow miw = new MyInfoWindow(R.layout.layout_balloon_for_marker, _mapView);
            marker.setInfoWindow(null);
            GeoPoint position = new GeoPoint(user.getLat(), user.getLon());
            marker.setPosition(position);
            int pos = i;
            miw.getView().setOnTouchListener((v, event) -> {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(listener != null){
                        animateToUser(pos);
                    }
                }
                return true;
            });
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                if(listener != null)
                    listener.onClick(user);
                return true;
            });
            int offsetX = Math.round(pxFromDp(_mapView.getContext(), 45));
            int offsetY = Math.round(pxFromDp(_mapView.getContext(), 55));
            miw.open(marker, position, offsetX, offsetY);
            miw.getView().<TextView>findViewById(R.id.name).setText(user.getName());
            miw.getView().<TextView>findViewById(R.id.time).setText(sdf.format(user.getDate()));
            overlays.add(marker);
        }
        _mapView.getOverlays().addAll(overlays);

        if (overlays.size() > 0) {
            userInFocusIndex = 0;
        } else {
            userInFocusIndex = -1;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateMeLocation(User user){
        _mapView.getOverlays().remove(meItem);
        OverlayItem item = new OverlayItem(null, null, new GeoPoint(user.getLat(), user.getLon()));
        List<OverlayItem> items = new ArrayList<>();
        items.add(item);
        item.setMarker(_mapView.getResources().getDrawable(R.drawable.ic_my_tracker_46dp, _mapView.getContext().getTheme()));
        meItem = new ItemizedIconOverlay<>(_mapView.getContext(), items, null);
        _mapView.getOverlays().add(meItem);
    }

    public void animateToUser(){
        if(overlays != null && overlays.size() > 0 && userInFocusIndex >= 0){
            _mapView.getController().animateTo(overlays.get(userInFocusIndex).getPosition());
            if(userInFocusIndex < overlays.size() - 1){
                userInFocusIndex++;
            } else{
                userInFocusIndex = 0;
            }
        }
    }

    private void animateToUser(int index){
        if(overlays != null && index >= 0 && index < overlays.size()){
            _mapView.getController().animateTo(overlays.get(index).getPosition());
        }
    }

    public void animateToMe(){
        if(meItem != null && meItem.size() > 0) {
            _mapView.getController().animateTo(meItem.getItem(0).getPoint());
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable builtMarker(@DrawableRes int avatarRes){
        Resources resources = _mapView.getResources();

        LayerDrawable drawableBack = ((LayerDrawable) ContextCompat.getDrawable(_mapView.getContext(), R.drawable.map_target_with_avatar));
        assert drawableBack != null;

        Rect bounds = drawableBack.findDrawableByLayerId(R.id.drawable_avatar).copyBounds();
        Drawable avatar = resources.getDrawable(avatarRes, _mapView.getContext().getTheme());
        avatar.setBounds(bounds);

        Bitmap avatarBitmap = drawableToBitmap(avatar);
        avatarBitmap = getRoundedCornerBitmap(avatarBitmap, avatar.getIntrinsicWidth());
        avatar = new BitmapDrawable(resources, avatarBitmap);
        drawableBack.setDrawableByLayerId(R.id.drawable_avatar, avatar);

        return drawableBack;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPixelSize) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = roundPixelSize;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF,roundPx,roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public interface OnItemClick{
        void onClick(Data data);
    }

}
