package com.example.search;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.baidu.mapapi.model.LatLng;

public class LocationOfPhoto {
	private String name;
	private String branch_name;
	private LatLng location;
	private String city;
	private String region;
	private String addr;
	private String tel;
	private String reserve_url;
	private String busid;
	private String avg_rating,avg_price,categories,s_photo_url;
	
	public LocationOfPhoto() {
		name = "";
		branch_name = "";
		location = null;
		city = "";
		region = "";
		addr = "";
		tel = "";
		reserve_url = "";
		avg_rating = "";
		avg_price = "";
		categories = "";
		s_photo_url = "";
		busid = "";
	}
	public LocationOfPhoto(String name, String branch_name, LatLng location, String city, 
			String region, String addr, String tel, String reserve_url, String avg_rating,
			String avg_price,String categories,String s_photo_url,String busid){
		this.name = name;
		this.branch_name = branch_name;
		this.location = location;
		this.city = city;
		this.region = region;
		this.addr = addr;
		this.tel = tel;
		this.reserve_url = reserve_url;
		this.avg_rating = avg_rating;
		this.avg_price = avg_price;
		this.categories = categories;
		this.s_photo_url = s_photo_url;
		this.busid = busid;
	}
	public LocationOfPhoto(String name, String branch_name, double latitude, double longitude, String city, String region, String addr, 
			String tel, String reserve_url,  String avg_rating,
			String avg_price,String categories,String s_photo_url,String busid){
		this.name = name;
		this.branch_name = branch_name;
		location = new LatLng(latitude, longitude);
		this.city = city;
		this.region = region;
		this.addr = addr;
		this.tel = tel;
		this.reserve_url = reserve_url;
		this.reserve_url = reserve_url;
		this.avg_rating = avg_rating;
		this.avg_price = avg_price;
		this.categories = categories;
		this.s_photo_url = s_photo_url;
		this.busid = busid;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the branch_name
	 */
	public String getBranch_name() {
		return branch_name;
	}
	/**
	 * @param branch_name the branch_name to set
	 */
	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}
	/**
	 * @return the location
	 */
	public LatLng getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(LatLng location) {
		this.location = location;
	}
	
	public String getCity(){
		return city;
	}
	
	public void setCity(String city){
		this.city = city;
	}
	
	public String getRegion(){
		return region;
	}
	
	public void setRegion(String region){
		this.region = region;
	}
	
	public String getAddr(){
		return addr;
	}
	
	public void setAddr(String addr){
		this.addr = addr;
	}
	
	public String getTel(){
		return tel;
	}
	
	public void setTel(String tel){
		this.tel = tel;
	}
	
	public String getreserve_url(){
		return reserve_url;
	}
	
	public void setreserve_url(String reserve_url){
		this.reserve_url = reserve_url;
	}
	
	public String getAvg_rating(){
		return avg_rating;
	}
	
	public void setAvg_rating(String avg_rating){
		this.avg_rating = avg_rating;
	}
	public String getAvg_price(){
		return avg_price;
	}
	
	public void setAvg_price(String avg_price){
		this.avg_price = avg_price;
	}
	
	public String getCategories(){
		return categories;
	}
	
	public void setCategories(String categories){
		this.categories = categories;
	}
	
	public String getS_photo_url(){
		return s_photo_url;
	}
	
	public void setS_photo_url(String s_photo_url){
		this.s_photo_url = s_photo_url;
	}
	
	public String getBusid(){
		return busid;
	}
	
	public void setBusid(String busid){
		this.busid = busid;
	}
}