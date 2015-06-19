/*
 * Joseph Circulate
 * This is a simple program for joseph problem.
 *
 * Usage:
 *      joseph -h
 * 
 * Compiling with g++ (GCC) 4.9.2 in GNU/Linux 3.19.2-1-ARCH
 * Last modified: Tue Apr 7 16:30:56 CST 2015
 *
 */

#include <cstdlib>
#include <iostream>
#include <unistd.h>

using namespace std;

/* An element of the circulate link list.*/
template <typename Type> class Node {
    public:
        Type  elem;
        Node* next;
        Node(const Type& elem, Node* next) { (*this).elem = elem; (*this).next = next; }
};

/* Circulate link list.*/
template <typename Type> class Circulate {
    private:
        Node<Type>* current;
    public:
        Circulate(const Type& elem) { current = new Node<Type>(elem, NULL); current->next = current; }
        ~Circulate(void) { delete current; }

        /* Add a node to append circulate link list.*/
        void appendNode(const Type& elem) { current = current->next = new Node<Type>(elem, current->next); }
        /* Move pointer to next target point, "shift" express length of each movement.*/
        void nextPos(const int& shift) { for (int i = 1; i < shift; ++i) current = current->next; }

        /* Remove current node.*/
        Type removeNode() {
            Type        elemTemp = current->next->elem;
            Node<Type>* currTemp = current->next;
            current->next  = current->next->next;
            delete currTemp;
            return elemTemp;
        }

        /* This function aims to complete joseph problem, deliver "length" and "shift", return last element.*/
        Type josephLoop(const int& length, const int& shift) { 
            for (int i = length; i > 1;) {
                nextPos(shift);
                cout << length-(--i) << ": " << removeNode() << endl;
            }
            cout << "===end===" << endl;
            return current->elem;
        }
};

int main(int argc, char* argv[]) {
    int shift = 0, length = 0, flag = 0;
    int opt;

    if (argc == 1) {
        cout << "LENGTH= ";
        cin >> length;
        cout << "SHIFT= ";
        cin >> shift;
    }

    else while ((opt = getopt(argc, argv, "i:h")) != -1) {
        switch(opt) {
        case 'i':
            for (int i = 0; optarg[i]; ++i) {
                if (!flag && isdigit(optarg[i])) 
                    length = length * 10 + (optarg[i] - 0x30);
                else if (flag == 1 && isdigit(optarg[i])) 
                    shift  = shift  * 10 + (optarg[i] - 0x30);
                else if (!isdigit(optarg[i]) && optarg[i] == ',' && !flag) 
                    ++flag;
                else {
                    cerr << "ERROR in option, use '-h' to show more." << endl;
                    exit(EXIT_FAILURE);
                }
            }
            break;
        case ':':
        case '?': 
        case 'h':
            cerr << " joseph [option] <LENGTH>,<SHIFT>\n"
                 << " EXAMPLE:\n"
                 << "           joseph -i 41,3\n"
                 << "           joseph -h\n"
                 << "     -i    Run program with input LENGTH and SHIFT.\n"  
                 << "     -h    This information.\n"
                 << endl;
            exit(EXIT_SUCCESS);
        }
    }

    /* Check "shift" and "length".*/
    if (shift < 0 || length < 1) {
        cerr << "ERROR in 'shift' or 'length'" << endl;
        exit(EXIT_FAILURE);
    }
    /* Construct a circulate link list.*/
   	Circulate<int> jose(1);
    for (int i = 2; i <= length; ++i) {
        jose.appendNode(i);
    }
    cout << jose.josephLoop(length, shift) << endl;
    return EXIT_FAILURE;
}

