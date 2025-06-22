package inf311.grupo1.projetopratico;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.util.Date;

public class Contato implements Parcelable {
    public String nome;
    public String email;
    public String telefone;

    public String interesse;

    public String escola;

    public String serie;

    public String responsavel;

    public Date ultimo_contato;


    public Integer id;


    public Contato(String n,String e,String t,String r,String i,String s,String esc,Date d)
    {
        nome=n;
        email=e;
        telefone=t;
        responsavel=r;
        interesse=i;
        serie=s;
        escola = esc;
        ultimo_contato=d;

    }

    public Contato(Parcel in)
    {
          nome=in.readString();
          Log.w("my app","Nome é :" + nome);
          email=in.readString();
          telefone=in.readString();
          responsavel=in.readString();
          interesse=in.readString();
          serie=in.readString();
          escola = in.readString();
          ultimo_contato=new Date(in.readLong());
    }

    public Contato(JSONObject ob)
    {
        try
        {
            nome=ob.getString("nome");
            email=ob.getString("email");
            telefone=ob.getString("telefone");
            responsavel=ob.getString("campopersonalizado_1_compl_cont");;
            interesse="";
            //serie=s;
            escola = ob.getString("escolaorigem");//
            ultimo_contato=new Date();
            id=ob.getInt("id");
        }

        catch (org.json.JSONException j)
        {

        }

    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int i)
    {
         p.writeString(nome);
         p.writeString(email);
         p.writeString(telefone);
         p.writeString(responsavel);
         p.writeString(interesse);
         p.writeString(serie);
         p.writeString(escola);
         p.writeLong(ultimo_contato.getTime());
    }

    public static final Parcelable.Creator<Contato> CREATOR =new Parcelable.Creator<Contato>()
    {

        public Contato createFromParcel(Parcel in)
        {
            return new Contato(in);
        }

        public Contato[] newArray(int size)
        {
            return new Contato[size];
        }
    };
}
