package org.cen.vision.coordinates;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

public class Coordinates3d {
	// rapport d'�chelle sur l'axe x (pixels par m�tre)
	private double kx = 1;

	// rapport d'�chelle sur l'axe y (pixels par m�tre)
	private double ky = 1;

	// d�formation des pixels (0 pour des pixels carr�s)
	private double s = 0;

	// position x du point central de l'image de la cam�ra (en pixels)
	private double cx = 160;

	// position y du point central de l'image de la cam�ra (en pixels)
	private double cy = 120;

	// distance focale
	private double f = 2;

	// position de la cam�ra dans le rep�re de la table
	private Vector3d t = new Vector3d(0, 0, 0);

	// orientation de la cam�ra
	private Matrix3d r = new Matrix3d();

	private double angle = Math.toRadians(20);

	Matrix4d p = new Matrix4d();

	Matrix4d k;

	public Coordinates3d() {
		r.rotX(angle - Math.PI / 2);
		// matrice de projection
		k = new Matrix4d(kx, s, cx, 0, 0, ky, cy, 0, 0, 0, 1, 0, 0, 0, 0, 1);
		// matrice de distance focale
		Matrix4d m = new Matrix4d(f, 0, 0, 0, 0, f, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
		// matrice de positionnement de la cam�ra par rapport au rep�re de
		// l'aire de jeu
		Matrix4d rr = new Matrix4d(r, t, 1);
		k.mul(m);
		k.mul(rr);

		System.out.println(k);
		k.invert();
		System.out.println(k);

		Vector4d point = new Vector4d(160, 200, 1, 1);
		k.transform(point);
		// normalisation du vecteur par rapport � la coordonn�e w
		point.scale(1 / point.w);
		// projection sur le plan z = 0
		point.y += point.z / Math.tan(Math.toRadians(20));
		point.z = 0;
		System.out.println(point);
	}

	public static void main(String[] args) {
		final Coordinates3d c = new Coordinates3d();
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(320, 240));
		JFrame f = new JFrame();
		f.getContentPane().add(p);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Vector4d v = new Vector4d(e.getX(), e.getY(), 1, 1);
				System.out.println(v);
				c.k.transform(v);
				v.scale(1 / v.w);
				v.y += v.z / Math.tan(c.angle);
				v.z = 0;
				System.out.println(v);
			}
		});
		f.pack();
		f.setVisible(true);
	}
}
