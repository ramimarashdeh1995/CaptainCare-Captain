package com.captaincare.captan_care.Fragments.Notification;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationDetailsFragment extends Fragment {

    TextView txtOfferTitle, txtOfferDes, txtOfferCity, txtOfferAddress, txtCaptainName,
            txtCaptainRate, txtOfferEndTime, txtAddress, txtOfferPrice, pirce, addressdialog, txtTimeEnd, txtUserName;
    ImageView imgOffer, imgVip;
    CircleImageView imgPersonal;
    CardView cardView;
    Button btnAccept, btnFavorite, finishdialogcaptain, btnAcceptcaptain;
    private AlertDialog.Builder dialogDelete, sureDialog;

    LinearLayout typeOfferCaptain;
    CircleImageView imgProfile;


    public NotificationDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_details, container, false);
        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.customToolbar);
        TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
        ImageView imgBack = toolbar.findViewById(R.id.imgBack);
        txtTitle.setText(R.string.offer_details);
        imgBack.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, new NotificationFragment()).commit();
        });


        cardView = view.findViewById(R.id.cardMyAds);
        typeOfferCaptain = view.findViewById(R.id.typeOfferCaptain);

        txtOfferTitle = view.findViewById(R.id.txtOfferTitle);
        txtOfferDes = view.findViewById(R.id.txtOfferDes);
        txtOfferCity = view.findViewById(R.id.txtOfferCity);
        txtOfferAddress = view.findViewById(R.id.txtOfferAddress);
        txtOfferEndTime = view.findViewById(R.id.txtOfferEndTime);
        txtAddress = view.findViewById(R.id.txtAddress);
        pirce = view.findViewById(R.id.pirce);
        addressdialog = view.findViewById(R.id.addressdialog);
        txtTimeEnd = view.findViewById(R.id.txtTimeEnd);
        txtUserName = view.findViewById(R.id.txtUserName);


        imgVip = view.findViewById(R.id.imgVip);

        txtCaptainName = view.findViewById(R.id.txtCaptainName);
        txtOfferPrice = view.findViewById(R.id.txtOfferPrice);
        txtCaptainRate = view.findViewById(R.id.txtCaptainRate);


        imgPersonal = view.findViewById(R.id.imgPersonal);
        imgOffer = view.findViewById(R.id.imgOffer);

        imgProfile = view.findViewById(R.id.imgProfile);

        btnAccept = view.findViewById(R.id.btnAccept);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        btnAcceptcaptain = view.findViewById(R.id.btnAcceptcaptain);
        finishdialogcaptain = view.findViewById(R.id.finishdialogcaptain);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("ShareNotificationData", Context.MODE_PRIVATE);
        String Type = sharedPreferences.getString("type", "");

        if (Type.equals("offerVendor")) {
            //اخفي ال card view وحط مكانها ProgressBar لحتى يجيب الداتا من السيرفر
            cardView.setVisibility(View.VISIBLE);
            typeOfferCaptain.setVisibility(View.GONE);
            String OfferID = sharedPreferences.getString("OfferID", "");
            String UserID = sharedPreferences.getString("userID", "");

            RetrieveDetailsOffer(OfferID, UserID);

        } else if (Type.equals("offerCaptain")) {
            cardView.setVisibility(View.GONE);


            String OfferID = sharedPreferences.getString("OfferID", "");
            String UserID = sharedPreferences.getString("userID", "");
            String username = sharedPreferences.getString("userName", "");
            String userImage = sharedPreferences.getString("userImg", "");
            String Address = sharedPreferences.getString("Address", "");
            String lon = sharedPreferences.getString("lon", "");
            String lat = sharedPreferences.getString("lat", "");

            String pirce = sharedPreferences.getString("cost", "");
            String endTimehours = sharedPreferences.getString("hours", "");

            typeOfferCaptain.setVisibility(View.VISIBLE);
            TypeAcceptOfferCaptain(OfferID, UserID, Address, lon, lat, endTimehours, pirce, userImage, username);

        }


    }

    private void TypeAcceptOfferCaptain(String OfferID, String UserID,
                                        String Address, String lon, String lat, String TimeEnd, String pirce1, String userImage,
                                        String UserName) {

        Glide.with(getActivity()).load(userImage).into(imgProfile);
        txtUserName.setText(UserName);
        pirce.setText(pirce1 + " " + getString(R.string.jd));
        addressdialog.setText(Address);
        txtTimeEnd.setText(TimeEnd);

        finishdialogcaptain.setOnClickListener(v -> {
            //   System.exit(0);
        });
        btnAcceptcaptain.setOnClickListener(v -> {
            dialogSaveOfferCaptain(UserID, OfferID, Address, lon, lat);
        });

    }

    private void dialogSaveOfferCaptain(String VendorID, String OfferID, String venAddress, String lon, String lat) {
        dialogDelete = new AlertDialog.Builder(getActivity());
        dialogDelete.setTitle(getString(R.string.accept_offer_dialog) + " " + venAddress);
        dialogDelete.setIcon(R.drawable.ic_successfull);
        dialogDelete.setPositiveButton(getString(R.string.go_now), (dialog, which) -> {
            dialog.dismiss();
            SaveOfferCaptain(VendorID, OfferID, lon, lat);

        });
        dialogDelete.setNeutralButton(getString(R.string.backe), (dialog, which) -> {
            dialog.dismiss();

        });
        dialogDelete.show();
    }

    private void SaveOfferCaptain(String UserID, String OfferID, String lon, String lat) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SaveOfferCaptain,
                response -> {
                    try {
                        ReadJsonObject(response, lon, lat);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    ReadServerError(error);
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ven_id", UserID);
                params.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                params.put("offer_id_market", OfferID);
                return params;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    /**
     *
     */

    private void RetrieveDetailsOffer(String offerID, String userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SelectDetailsOffer,
                response -> {
                    try {
                        ReadResponseOffer(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ReadServerError(error);
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Parms = new HashMap<>();
                Parms.put("offer_id", offerID);
                Parms.put("ven_id", userID);
                Parms.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());

                return Parms;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void ReadResponseOffer(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getString("error").equals("false")) {
            ReadDetailsOffer(jsonObject.getJSONArray("message"));
        } else if (jsonObject.getString("error").equals("true")) {
            if (jsonObject.getString("message").equals("EndOffer")) {
                ViewTOAST(jsonObject.getString("message"));
                // This Offer End
            } else if (jsonObject.getString("message").equals("SaveOffer")) {
                // This Offer IN SaveProcess
                ViewTOAST(jsonObject.getString("message"));
            } else if (jsonObject.getString("message").equals("ERROR")) {
                // Error In Server
                ViewTOAST(jsonObject.getString("message"));
            } else {
                ViewTOAST(jsonObject.getString("message"));
            }
        }
    }

    private void ViewTOAST(String message) {
        cardView.setVisibility(View.GONE);
        Toast.makeText(
                getActivity(),
                message,
                Toast.LENGTH_LONG
        ).show();
    }

    private void ReadDetailsOffer(JSONArray message) throws JSONException {

        JSONObject object = message.getJSONObject(0);

        String OfferID = object.getString("offer_id");
        String UserID = object.getString("ven_id");
        String lon = object.getString("ven_lon");
        String lat = object.getString("ven_lat");
        String Address = object.getString("ven_address");


        txtOfferTitle.setText(object.getString("offer_title"));
        txtOfferDes.setText(object.getString("offer_disc"));
        txtOfferCity.setText(object.getString("city"));
        txtOfferAddress.setText(object.getString("ven_address"));
        txtOfferEndTime.setText(object.getString("offer_end"));
        ;
        txtAddress.setText(Address);


        txtCaptainName.setText(object.getString("ven_name"));
        txtOfferPrice.setText(object.getString("cost") + " " + getString(R.string.jd));
        txtCaptainRate.setText(object.getString("eval"));


        Glide.with(getActivity()).load(object.getString("offer_pic1")).into(imgOffer);
        Glide.with(getActivity()).load(object.getString("ven_photo_url")).into(imgPersonal);

        if (object.getString("isfav").equals("null")) {
            btnFavorite.setText(getString(R.string.add_to_favorite));
            btnFavorite.setSelected(false);
        } else {
            btnFavorite.setText(getString(R.string.remove));
            btnFavorite.setSelected(true);
        }

        btnFavorite.setOnClickListener(v -> {
            if (btnFavorite.getText().toString().equals(getString(R.string.add_to_favorite))) {
                btnFavorite.setText(getString(R.string.remove));
                btnFavorite.setSelected(true);
                addOfferFavoriteCaptain(OfferID, UserID);
            } else {
                btnFavorite.setText(getString(R.string.add_to_favorite));
                btnFavorite.setSelected(false);

                removeOfferFavoriteCaptain(OfferID);
            }
        });

        btnAccept.setOnClickListener(v -> {
            btnAccept.setEnabled(false);
            dialogAcceptOfferVendorFromCaptain(UserID, OfferID, lon, lat, Address);
            btnAccept.setEnabled(true);
        });

    }

    private void dialogAcceptOfferVendorFromCaptain(String VendorID, String OfferID, String lon, String lat, String venAddress) {

        sureDialog = new AlertDialog.Builder(getActivity());
        sureDialog.setTitle("Are you sure to accept the offer and go to : ");
        sureDialog.setMessage(venAddress);
        sureDialog.setPositiveButton("Go Now", (dialog, which) -> {
            dialog.dismiss();
            acceptOfferVendorFromCaptainNow(VendorID, OfferID, lon, lat);
        });
        sureDialog.setNegativeButton("Go Last", (dialog, which) -> {
            dialog.dismiss();
            acceptOfferVendorFromCaptainLast(VendorID, OfferID);
        });
        sureDialog.setNeutralButton("Back", (dialog, which) -> dialog.dismiss());
        sureDialog.create().show();

    }

    private void addOfferFavoriteCaptain(String offer_id, String venID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.AddFavoriteOffer,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {

                        } else if (jsonObject.getString("error").equals("true")) {

                        }
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                , error -> {
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                parms.put("offer_id", offer_id);
                parms.put("ven_id", venID);
                return parms;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void removeOfferFavoriteCaptain(String offer_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.UnFavoriteOffer,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {

                        } else if (jsonObject.getString("error").equals("true")) {

                        }
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                , error -> {
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                parms.put("offer_id", offer_id);
                return parms;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void acceptOfferVendorFromCaptainLast(String VendorID, String OfferID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SaveOfferLastGo,
                response -> {
                    try {
                        ReadJsonObject(response, "null", "null");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                parms.put("ven_id", VendorID);
                parms.put("offer_id", OfferID);
                return parms;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void acceptOfferVendorFromCaptainNow(String VendorID, String OfferID, String lon, String lat) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SaveOfferMarket,
                response -> {
                    try {
                        ReadJsonObject(response, lon, lat);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                parms.put("ven_id", VendorID);
                parms.put("offer_id_market", OfferID);
                return parms;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    private void ReadJsonObject(String response, String lon, String lat) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getString("error").equals("false")) {
            dialogDelete = new AlertDialog.Builder(getActivity());
            dialogDelete.setTitle("Successful Deleting");
            dialogDelete.setIcon(R.drawable.ic_successfull);
            dialogDelete.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                dialog.dismiss();
                if (lon.equals("null") && lat.equals("null")) {
                    if (ShareProfileData.getInstance(getActivity()).IsLogInCaptain()) {
                        Toast.makeText(getActivity(), "Current Offer", Toast.LENGTH_LONG).show();
                        System.exit(0);
                    }
                } else {
                    Uri navigationIntentUri = Uri.parse("google.navigation:q=" + lat +
                            "," + lon);//creating intent with latlng
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                    System.exit(0);
                }
            });
            dialogDelete.show();
        } else if (jsonObject.getString("error").equals("true")) {
            if (jsonObject.getString("message").equals("your not have CC")) {
                dialogDelete = new AlertDialog.Builder(getActivity());
                dialogDelete.setTitle("your not have CC");
                dialogDelete.setIcon(R.drawable.ic_error2);
                dialogDelete.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                    dialog.dismiss();
                    // getActivity().finish();
                });
                dialogDelete.show();
            } else if (jsonObject.getString("message").equals("your accept offer")) {
                dialogDelete = new AlertDialog.Builder(getActivity());
                dialogDelete.setTitle("your Accept the offer");
                dialogDelete.setIcon(R.drawable.ic_error2);
                dialogDelete.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                    dialog.dismiss();
                    //  getActivity().finish();
                });
                dialogDelete.show();
            }
            dialogDelete = new AlertDialog.Builder(getActivity());
            dialogDelete.setTitle("Server Error");
            dialogDelete.setIcon(R.drawable.ic_error2);
            dialogDelete.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                dialog.dismiss();
                //  getActivity().finish();
            });
            dialogDelete.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbarVisibility(View.GONE);
    }

    private void ReadServerError(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            //This indicates that the reuest has either time out or there is no connection

        } else if (error instanceof AuthFailureError) {
            //Error indicating that there was an Authentication Failure while performing the request

        } else if (error instanceof ServerError) {
            //Indicates that the server responded with a error response

        } else if (error instanceof NetworkError) {
            //Indicates that there was network error while performing the request

        } else if (error instanceof ParseError) {
            // Indicates that the server response could not be parsed

        }
    }


}
/*
    * {
    "error": false,
    "message": [
        {
            "offer_id": 72,
            "ven_id": 1,
            "sub_id": 1,
            "city": "Irbid",
            "offer_title": "عرض ",
            "offer_disc": "وةىىرر",
            "offer_pic1": "https://captain-care.org/ccapp/OfferMarket/ImageOfferMarket/insert1120190601144807.png",
            "offer_pic2": "",
            "offer_pic3": "",
            "offer_pic4": "",
            "offer_pic5": "",
            "offer_pic6": "",
            "offer_start": "2019-06-01 14:48:07",
            "offer_end": "2019-06-27 18:48:07",
            "isEnd": 0,
            "ven_name": "3qoul alkafo",
            "ven_address": "",
            "ven_mobile": "+962791921227",
            "ven_city": "Ajloun",
            "ven_photo_url": "http://captain-care.org/ccapp/control_market/ImageProfile/24.png",
            "ven_token": "eCauppDYX4w:APA91bFiId771kYxUOBgntZBy1WcYpdKId7QDVNxHjcIX3QmyA8PD1bocomm5nD4Ky0ZeYP-mhhvGR4xODRgvDw95aWiptF-TbgCtApaA7QENirLSpQVKc1MpQd4ueoVVjlk9AwOK0t2",
            "ven_lon": -122.082999999999998408384271897375583648681640625,
            "ven_lat": 37.421999999999997044142219237983226776123046875,
            "isfav": null,
            "isPaid": 1,
            "cost": 555,
            "eval": 3.3300000000000000710542735760100185871124267578125
        }
    ]
}
    * */