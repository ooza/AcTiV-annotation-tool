# `AcTiV-GT` an annotation tool for text detection and recognition in videos and scene images  

## Documentation
The creation of any standard database should undergo a manual or semiautomatic annotation process to generate ground-truth information for every text region.
In our case, we have used the AcTiV-GT tool to annotate Arabic text regions in news videos.
The annotation process consists of two levels: global and local.
* **The global annotation** process is performed manually through a user-friendly interface:

![AcTiV-GT](/images/AcTiV-GT-UI.png)
  
During the annotation process, we first load a video sequence. Then we
draw a rectangular bounding-box for each textline that has a uniform size, alignment
and spacing. Once a rectangle is selected, a new set of information will be created. It
contains the following elements: *Position*: x, y, width and height. *Content*: text strings, text color, background color, background type (transparent,
opaque). This set of information, along with the apparition and disapparition frame number of each textline, are saved in the *global XML file*:

![Global](/images/PartofaglobalXMLfile.png)

Dynamic text, in news content, is composed of scrolling series of tickers. To annotate this type of text, we note for each ticker its content, the first frame where the ticker appears, and the initial offset in the first frame, which is estimated using a virtual line. This information
is stored in the *scrollingText* element of the global XML file.

* **The local annotation** is performed at the frame level according to the information contained in the *global XML file*. Two appropriate types of XML files are
generated, one for the detection task and the other for the recognition task. Hereafter an extract of *detection XML file* of France24 TV channel:

![D](/images/xmlD.png)

The ground-truth information of the detection task are provided at the line level for each frame. The following figure depcits an example of a ground-truth XML file and its corresponding textline image:

![R](/images/xmlR.png)

## Citation
If you use `AcTiV-GT` please use the following citation

```bibtex
@inproceedings{zayene2014semi,
  title={Semi-automatic news video annotation framework for Arabic text},
  author={Zayene, Oussama and Touj, Sameh Masmoudi and Hennebert, Jean and Ingold, Rolf and Amara, Najoua Essoukri Ben},
  booktitle={2014 4th International Conference on Image Processing Theory, Tools and Applications (IPTA)},
  pages={1--6},
  year={2014},
  organization={IEEE}
}
```

