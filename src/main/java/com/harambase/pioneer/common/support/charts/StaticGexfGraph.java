package com.harambase.pioneer.common.support.charts;

import com.harambase.pioneer.common.constant.UserType;
import com.harambase.pioneer.server.core.pojo.base.Advise;
import com.harambase.pioneer.server.core.pojo.base.Person;
import com.harambase.pioneer.server.core.pojo.base.Transcript;
import com.harambase.pioneer.server.core.pojo.view.CourseView;
import it.uniroma1.dis.wsngroup.gexf4j.core.*;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.viz.PositionImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.viz.NodeShape;
import it.uniroma1.dis.wsngroup.gexf4j.core.viz.Position;

import java.io.*;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class StaticGexfGraph {

    public static String graphGenerator(List<Person> personList,
                                        List<LinkedHashMap> courseViewList,
                                        List<Transcript> transcriptList,
                                        List<Advise> adviseList) {
        Gexf gexf = new GexfImpl();
        Calendar date = Calendar.getInstance();

        gexf.getMetadata()
                .setLastModified(date.getTime())
                .setCreator("server.org")
                .setDescription("A relation graph");
        gexf.setVisualization(true);

        Graph graph = gexf.getGraph();
        graph.setDefaultEdgeType(EdgeType.UNDIRECTED).setMode(Mode.STATIC);

        AttributeList attrList = new AttributeListImpl(AttributeClass.NODE);
        graph.getAttributeLists().add(attrList);

        Attribute attType = attrList.createAttribute("type", AttributeType.INTEGER, "type");
        Attribute attValue = attrList.createAttribute("value", AttributeType.STRING, "value");
        Attribute attID = attrList.createAttribute("id", AttributeType.STRING, "id");
        Attribute attCRN = attrList.createAttribute("crn", AttributeType.STRING, "crn");
        int index = 0;
        //设置person node
        for (Person p : personList) {
            Node pNode = graph.createNode(p.getUserId());
            pNode.setLabel(p.getLastName() + p.getFirstName()).setSize(20);
            int type = 0;
            if (p.getType().contains("s")) {
                pNode.getAttributeValues()
                        .addValue(attID, p.getUserId())
                        .addValue(attType, String.valueOf(UserType.STUDENT.getV()))
                        .addValue(attValue, p.getInfo());
                type = UserType.STUDENT.getV();
            } else if (p.getType().contains("f")) {
                pNode.getAttributeValues()
                        .addValue(attID, p.getUserId())
                        .addValue(attType, String.valueOf(UserType.FACULTY.getV()))
                        .addValue(attValue, p.getInfo());
                type = UserType.FACULTY.getV();
            } else if (p.getType().contains("a")) {
                pNode.getAttributeValues()
                        .addValue(attID, p.getUserId())
                        .addValue(attType, String.valueOf(UserType.ADMINISTRATOR.getV()))
                        .addValue(attValue, p.getInfo());
                pNode.setSize(0);
                type = UserType.ADMINISTRATOR.getV();
            }

            pNode.getShapeEntity().setNodeShape(NodeShape.DIAMOND);
            pNode.setPosition(generatePosition(type, index + 1));
            index++;
        }
        //设置course Node
        for (LinkedHashMap c : courseViewList) {
            Node cNode = graph.createNode((String)c.get("crn"));
            int value = (int) c.get("capacity") - (int) c.get("remain");
            cNode.setLabel((String)c.get("name")).setSize(10 + value * 5);
            cNode.getAttributeValues()
                    .addValue(attCRN, (String)c.get("crn"))
                    .addValue(attValue, String.valueOf(value))
                    .addValue(attType, "0");

            cNode.getShapeEntity().setNodeShape(NodeShape.TRIANGLE);
            cNode.setPosition(generatePosition(0, index));
            index++;
        }

        index = 0;
        //设置COURSE_PERSON_connection
        for (Person p : personList) {
            String userid = p.getUserId();
            String type = p.getType();
            Node pNode = graph.getNode(userid);
            if (type.contains("s")) {
                for (Transcript t : transcriptList) {
                    if (t.getStudentId().equals(userid)) {
                        Node cNode = graph.getNode(t.getCrn());
                        pNode.connectTo(String.valueOf(index), cNode);
                        index++;
                    }
                }
            } else if (type.contains("f")) {
                for (LinkedHashMap c : courseViewList) {
                    if (((String)c.get("facultyId")).equals(userid)) {
                        Node cNode = graph.getNode((String) c.get("crn"));
                        pNode.connectTo(String.valueOf(index), cNode);
                        index++;
                    }
                }
            }
        }

        //设置FACULTY_STUDENT_CONNECTION
        for (Advise a : adviseList) {
            Node sNode = graph.getNode(a.getStudentId());
            Node fNode = graph.getNode(a.getFacultyId());
            sNode.connectTo(String.valueOf(index), fNode);
            index++;
        }

        StaxGraphWriter graphWriter = new StaxGraphWriter();
        String path = StaticGexfGraph.class.getResource("").getPath();


        try {
            File f = new File(path + "/static_graph_sample.gexf");
            f.deleteOnExit();
            Writer out = new FileWriter(f, false);

            graphWriter.writeToStream(gexf, out, "UTF-8");

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = "";
            StringBuilder buffer = new StringBuilder();
            while ((line = br.readLine()) != null)
                buffer.append(line);

            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Position generatePosition(int type, int index) {
        PositionImpl position = new PositionImpl();
        double x = 10 * type + index;
        double y = 500 + index * 2;

        position.setX(Float.parseFloat(String.valueOf(x)));
        position.setY(Float.parseFloat(String.valueOf(y)));

        return position;
    }
}
