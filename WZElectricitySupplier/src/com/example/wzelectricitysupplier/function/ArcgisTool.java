package com.example.wzelectricitysupplier.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.FontStyle;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleLineSymbol.STYLE;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.database.CircuitTable;
import com.example.wzelectricitysupplier.fragment.MainFragment;
import com.example.wzelectricitysupplier.listener.MyOnSingleTapListener.AddMode;

public class ArcgisTool {
	
	public static final String ATTRIBUTE_KEY_ID = "ID";
	public static final String ATTRIBUTE_KEY_TYPE = "Type";
	
	public static int getGraphicId(MapView map, float screenx, float screeny, GraphicsLayer layer) {
		int graphicId = -1;
		int[] graphicIDs = layer.getGraphicIDs(screenx, screeny, 10);
		if(graphicIDs.length>0)
			graphicId = graphicIDs[0];
		return graphicId;
	}
	
	public static int addGraphic(Geometry geometry, int imageRes, GraphicsLayer layer){
		int graphicId = -1;
		if(layer !=null){
			Drawable drawable = MyApplication.Resources.getDrawable(imageRes);
			PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
//			symbol.setOffsetY(symbol.getHeight()/8); // 负数向上 正数像下
			Map<String, Object> attributes = new HashMap<String, Object>();
			Graphic graphic = new Graphic(geometry, symbol, attributes);
			graphicId = layer.addGraphic(graphic);
		}
		return graphicId;
	}
	
	public static int addTextGraphic(Point point, String text, MapView map, GraphicsLayer textLayer, int resID){
		if(textLayer!=null){
			TextSymbol textSymbol = new TextSymbol(14, text, MyApplication.Resources.getColor(resID));
			textSymbol.setOffsetX(13);	
			textSymbol.setOffsetY(-10);	
			textSymbol.setFontStyle(FontStyle.NORMAL);
			textSymbol.setFontFamily("DroidSansFallback.ttf");
			textSymbol.setSize(15f);
			return textLayer.addGraphic(new Graphic(point, textSymbol, null));
		}
		return -1;
	}
	
	public static int addTextGraphic(Point point, String text, MapView map, GraphicsLayer textLayer, int x, int y){
		if(textLayer!=null){
			TextSymbol textSymbol = new TextSymbol(14, text, MyApplication.Resources.getColor(R.color.line_color2));
			textSymbol.setOffsetX(13+x);	
			textSymbol.setOffsetY(-10-y);	
			textSymbol.setFontStyle(FontStyle.NORMAL);
			textSymbol.setFontFamily("DroidSansFallback.ttf");
			textSymbol.setSize(15f);
			return textLayer.addGraphic(new Graphic(point, textSymbol, null));
		}
		return -1;
	}
	
	public static int addTextGraphic(Point point, String text, MapView map, GraphicsLayer textLayer){
		return addTextGraphic(point, text, map, textLayer, R.color.line_color2);
	}
	
	public static int addLineGraphic(Geometry geometry, Symbol symbol, GraphicsLayer layer){
		int graphicId = -1;
		if(layer !=null){
			Map<String, Object> attributes = new HashMap<String, Object>();
			Graphic graphic = new Graphic(geometry, symbol, attributes);
			graphicId = layer.addGraphic(graphic);
		}
		return graphicId;
	}
	
	
	public static void updateGraphic(int graphicId, GraphicsLayer graphicsLayer, int primaryId, AddMode mode){
		Graphic graphic = graphicsLayer.getGraphic(graphicId);
		Map<String, Object> attributes = graphic.getAttributes();
		attributes.put(ATTRIBUTE_KEY_ID, primaryId);
		attributes.put(ATTRIBUTE_KEY_TYPE, mode);
		graphicsLayer.updateGraphic(graphicId, attributes);
	}
	
	public static void removeGraphic(float screenx, float screeny, MapView map, GraphicsLayer layer){
		if(layer !=null){
			int id = getGraphicId(map, screenx, screeny, layer);
			layer.removeGraphic(id);
		}
	}
	
	public static void removeGraphic(int graphicId, GraphicsLayer layer){
		if(layer !=null){
			Graphic graphic = layer.getGraphic(graphicId);
			if(graphic!=null){
				layer.removeGraphic(graphicId);
			}
		}
	}
	
	public static Symbol getLineFourSymbol(String type){
		STYLE style = null;
		if(type == null){
			style = SimpleLineSymbol.STYLE.SOLID;
		}else{
			switch(type){
			case "绝缘导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			case "电缆":
				style = SimpleLineSymbol.STYLE.DASH;
				break;
			case "裸导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			}
		}
		SimpleLineSymbol symbol1 = new SimpleLineSymbol(MyApplication.Resources.getColor(R.color.line_color1), 15, style);
		SimpleLineSymbol symbol2 = new SimpleLineSymbol(Color.WHITE, 12, SimpleLineSymbol.STYLE.SOLID);
		SimpleLineSymbol symbol3 = new SimpleLineSymbol(MyApplication.Resources.getColor(R.color.line_color1), 6, style);
		SimpleLineSymbol symbol4 = new SimpleLineSymbol(Color.WHITE, 3, SimpleLineSymbol.STYLE.SOLID);
		CompositeSymbol symbol = new CompositeSymbol();
		symbol.add(symbol1);
		symbol.add(symbol2);
		symbol.add(symbol3);
		symbol.add(symbol4);
		return symbol;
	}
	public static Symbol getLineTwoSymbol(String type){
		STYLE style = null;
		if(type == null){
			style = SimpleLineSymbol.STYLE.SOLID;
		}else{
			switch(type){
			case "绝缘导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			case "电缆":
				style = SimpleLineSymbol.STYLE.DASH;
				break;
			case "裸导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			}
		}
		SimpleLineSymbol symbol1 = new SimpleLineSymbol(MyApplication.Resources.getColor(R.color.line_color1), 6, style);
		SimpleLineSymbol symbol2 = new SimpleLineSymbol(Color.WHITE, 3, SimpleLineSymbol.STYLE.SOLID);
		CompositeSymbol symbol = new CompositeSymbol();
		symbol.add(symbol1);
		symbol.add(symbol2);
		return symbol;
	}
	public static Symbol getLineOneSymbol(String type, int index){
		STYLE style = null;
		if(type == null){
			style = SimpleLineSymbol.STYLE.SOLID;
		}else{
			switch(type){
			case "绝缘导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			case "电缆":
				style = SimpleLineSymbol.STYLE.DASH;
				break;
			case "裸导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			}
		}
		SimpleLineSymbol mSimpleLineSymbol = null;
		int color = getColorByIndex(index);
		CompositeSymbol mCompositeSymbol = new CompositeSymbol();
		if(color !=-1){
			mSimpleLineSymbol = new SimpleLineSymbol(color, 2, style);
			mCompositeSymbol.add(mSimpleLineSymbol);
		}
		return mCompositeSymbol;
	}
	public static int getColorByIndex(int index){
		int color = -1;
		switch(index){
		case 0:
			color = MyApplication.Resources.getColor(R.color.red);
			break;
		case 1:
			color = MyApplication.Resources.getColor(R.color.blue);
			break;
		case 2:
			color = MyApplication.Resources.getColor(R.color.green);
			break;
		case 3:
			color = MyApplication.Resources.getColor(R.color.yellow);
			break;
		case 4: // 找不到对应的线路名称的情况下
			color = MyApplication.Resources.getColor(R.color.gray_light);
			break;
		}
		return color;
	}
	
	// 电线名称由终点设备决定（优先） 或由起点设备决定
	public static Symbol judgeLineOneSymbolColor(MainFragment mMainFragment, String type, String deviceName1, String deviceName2){
		Symbol symbol = null;
		CircuitTable mCircuitTable = mMainFragment.getCircuitTable();
		List<String> mBranchNames = mCircuitTable.getBranchName();
		String mDxName2 = deviceName1.split(" ")[0].trim();
		String mDxName1 = deviceName2.split(" ")[0].trim();
		int index = mBranchNames.indexOf(mDxName2);
		if(index==-1){
			index = mBranchNames.indexOf(mDxName1);
		}
		if(index==-1){
			index = 4;
		}
		symbol = ArcgisTool.getLineOneSymbol(type, index);
		return symbol;
	}
	
	public static int accordingNameGetDrawableResource(String name){
		int resid = -1;
		switch(name){
		case "g0":
			resid = R.drawable.g0;
			break;
		case "g1":
			resid = R.drawable.g1;
			break;
		case "g2":
			resid = R.drawable.g2;
			break;
		case "g3":
			resid = R.drawable.g3;
			break;
		case "g4":
			resid = R.drawable.g4;
			break;
		case "g5":
			resid = R.drawable.g5;
			break;
		case "g6":
			resid = R.drawable.g6;
			break;
		case "g7":
			resid = R.drawable.g7;
			break;
		case "g8":
			resid = R.drawable.g8;
			break;
		case "g9":
			resid = R.drawable.g9;
			break;
		case "g10":
			resid = R.drawable.g10;
			break;
		case "g11":
			resid = R.drawable.g11;
			break;
		case "g12":
			resid = R.drawable.g12;
			break;
		case "g13":
			resid = R.drawable.g13;
			break;
		case "g14":
			resid = R.drawable.g14;
			break;
		case "g15":
			resid = R.drawable.g15;
			break;
		case "g16":
			resid = R.drawable.g16;
			break;
		case "g17":
			resid = R.drawable.g17;
			break;
		case "g18":
			resid = R.drawable.g18;
			break;
		case "g19":
			resid = R.drawable.g19;
			break;
		case "g20":
			resid = R.drawable.g20;
			break;
		case "g_more":
			resid = R.drawable.g_more;
			break;
		case "y0":
			resid = R.drawable.y0;
			break;
		case "y1":
			resid = R.drawable.y1;
			break;
		case "y2":
			resid = R.drawable.y2;
			break;
		case "y3":
			resid = R.drawable.y3;
			break;
		case "y4":
			resid = R.drawable.y4;
			break;
		case "y5":
			resid = R.drawable.y5;
			break;
		case "y6":
			resid = R.drawable.y6;
			break;
		case "y7":
			resid = R.drawable.y7;
			break;
		case "y8":
			resid = R.drawable.y8;
			break;
		case "y9":
			resid = R.drawable.y9;
			break;
		case "y10":
			resid = R.drawable.y10;
			break;
		case "y11":
			resid = R.drawable.y11;
			break;
		case "y12":
			resid = R.drawable.y12;
			break;
		case "y13":
			resid = R.drawable.y13;
			break;
		case "y14":
			resid = R.drawable.y14;
			break;
		case "y15":
			resid = R.drawable.y15;
			break;
		case "y16":
			resid = R.drawable.y16;
			break;
		case "y17":
			resid = R.drawable.y17;
			break;
		case "y18":
			resid = R.drawable.y18;
			break;
		case "y19":
			resid = R.drawable.y19;
			break;
		case "y20":
			resid = R.drawable.y20;
			break;
		case "y_more":
			resid = R.drawable.y_more;
			break;
		case "r0":
			resid = R.drawable.r0;
			break;
		case "r1":
			resid = R.drawable.r1;
			break;
		case "r2":
			resid = R.drawable.r2;
			break;
		case "r3":
			resid = R.drawable.r3;
			break;
		case "r4":
			resid = R.drawable.r4;
			break;
		case "r5":
			resid = R.drawable.r5;
			break;
		case "r6":
			resid = R.drawable.r6;
			break;
		case "r7":
			resid = R.drawable.r7;
			break;
		case "r8":
			resid = R.drawable.r8;
			break;
		case "r9":
			resid = R.drawable.r9;
			break;
		case "r10":
			resid = R.drawable.r10;
			break;
		case "r11":
			resid = R.drawable.r11;
			break;
		case "r12":
			resid = R.drawable.r12;
			break;
		case "r13":
			resid = R.drawable.r13;
			break;
		case "r14":
			resid = R.drawable.r14;
			break;
		case "r15":
			resid = R.drawable.r15;
			break;
		case "r16":
			resid = R.drawable.r16;
			break;
		case "r17":
			resid = R.drawable.r17;
			break;
		case "r18":
			resid = R.drawable.r18;
			break;
		case "r19":
			resid = R.drawable.r19;
			break;
		case "r20":
			resid = R.drawable.r20;
			break;
		case "r_more":
			resid = R.drawable.r_more;
			break;
		default:
			resid = R.drawable.b0;
			break;
		}
		return resid;
	}
}
